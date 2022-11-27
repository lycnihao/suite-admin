package run.bottle.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.model.vo.PermissionVo;
import run.bottle.admin.service.PermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission controller
 *
 * @author liyc
 * @date 2022-11-27
 */
@RestController
public class PermissionController {

	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@GetMapping("/permission/getAllPermissions")
	public BaseResponse<List<PermissionVo>> listAllPermission() {
		List<Permission> permissions = permissionService.getPermissions();
		List<Permission> parentPermissions = permissions.stream().filter(permission -> permission.getParentId() != null && permission.getParentId() <= 0).collect(Collectors.toList());
		return BaseResponse.ok(toPermissionVo(parentPermissions, permissions));
	}

	private List<PermissionVo> toPermissionVo(List<Permission> permissions, List<Permission> allPermissions) {
		List<PermissionVo> menuVos = new ArrayList<>();
		for (Permission permission : permissions) {
			PermissionVo permissionVo = new PermissionVo();
			permissionVo.setName(permission.getName());
			permissionVo.setTitle(permission.getTitle());
			List<Permission> childrenList = allPermissions.stream()
					.filter(p -> p.getParentId().equals(permission.getId()))
					.collect(Collectors.toList());
			if (!permissions.isEmpty()) {
				List<PermissionVo> children = toPermissionVo(childrenList, allPermissions);
				permissionVo.setChildren(children);
			}
			menuVos.add(permissionVo);
		}
		return menuVos;
	}
}
