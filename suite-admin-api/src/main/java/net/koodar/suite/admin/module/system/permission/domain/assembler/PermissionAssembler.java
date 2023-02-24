package net.koodar.suite.admin.module.system.permission.domain.assembler;

import org.springframework.stereotype.Component;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.permission.domain.PermissionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission assembler.
 *
 * @author liyc
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
