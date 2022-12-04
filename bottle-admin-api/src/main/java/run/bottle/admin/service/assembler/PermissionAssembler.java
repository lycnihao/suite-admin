package run.bottle.admin.service.assembler;

import org.springframework.stereotype.Component;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.vo.PermissionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission assembler.
 *
 * @author liyc
 * @date 2022-11-30
 */
@Component
public class PermissionAssembler {

	public List<PermissionVo> toPermissionTree(List<Permission> permissions, List<Permission> allPermissions) {
		List<PermissionVo> menuVos = new ArrayList<>();
		for (Permission permission : permissions) {
			PermissionVo permissionVo = new PermissionVo();
			permissionVo.setId(permission.getId());
			permissionVo.setName(permission.getName());
			permissionVo.setTitle(permission.getTitle());
			permissionVo.setType(permission.getType());
			permissionVo.setIcon(permission.getIcon());
			permissionVo.setPath(permission.getPath());
			permissionVo.setRedirect(permission.getRedirect());
			permissionVo.setComponent(permission.getComponent());
			permissionVo.setSort(permission.getSort());
			permissionVo.setKeepAlive(permission.getKeepAlive());
			List<Permission> childrenList = allPermissions.stream()
					.filter(p -> p.getParentId().equals(permission.getId()))
					.collect(Collectors.toList());
			if (!permissions.isEmpty()) {
				List<PermissionVo> children = toPermissionTree(childrenList, allPermissions);
				children.forEach(child -> child.setParentName(permissionVo.getName()));
				permissionVo.setChildren(children);
			}
			menuVos.add(permissionVo);
		}
		return menuVos;
	}

}
