package run.bottle.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.model.vo.PermissionMetaVo;
import run.bottle.admin.model.vo.MenuVo;
import run.bottle.admin.security.support.AppUserDetails;
import run.bottle.admin.security.util.SecurityContextUtils;
import run.bottle.admin.service.PermissionService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyc
 * @date 2022-11-02
 */
@RestController
public class MenusController {

	private final PermissionService permissionService;

	public MenusController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}


	/**
	 * 获取当前用户菜单，用于前端动态生成菜单
	 *
	 * @return 菜单
	 */
	@GetMapping("/menus")
	public BaseResponse<Collection<MenuVo>> getMenus() {
		AppUserDetails userDetails = SecurityContextUtils.getUserDetails();
		List<Permission> permissions = permissionService.getMenusByRoleIds(userDetails.getRoleIds());
		List<Permission> parentPermissions = permissions.stream().filter(permission -> permission.getParentId() != null && permission.getParentId() <= 0).collect(Collectors.toList());
		return BaseResponse.ok(toMenusList(parentPermissions, permissions));
	}

	private List<MenuVo> toMenusList(List<Permission> permissions, List<Permission> allPermissions) {
		List<MenuVo> menuVos = new ArrayList<>();
		for (Permission permission : permissions) {
			MenuVo menuVo = toMenus(permission);
			List<Permission> childrenList = allPermissions.stream()
					.filter(p -> p.getParentId().equals(permission.getId()))
					.collect(Collectors.toList());
			if (!permissions.isEmpty()) {
				List<MenuVo> childrenMenus = toMenusList(childrenList, allPermissions);
				menuVo.setChildren(childrenMenus);
			}
			menuVos.add(menuVo);
		}
		return menuVos;
	}

	private MenuVo toMenus(Permission permission) {
		MenuVo menuVo = new MenuVo();
		menuVo.setName(permission.getName());
		menuVo.setTitle(permission.getTitle());
		menuVo.setPath(permission.getPath());
		menuVo.setComponent(permission.getComponent());
		PermissionMetaVo permissionMetaVo = new PermissionMetaVo();
		permissionMetaVo.setTitle(permission.getTitle());
		permissionMetaVo.setIcon(permission.getIcon());
		permissionMetaVo.setSort(permission.getSort());
		permissionMetaVo.setKeepAlive(permission.getKeepAlive());
		menuVo.setMeta(permissionMetaVo);
		menuVo.setChildren(new ArrayList<>());
		return menuVo;
	}

}
