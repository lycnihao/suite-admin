package net.koodar.suite.admin.module.system.role.service;

import net.koodar.suite.admin.module.system.role.repository.RolePermissionRepository;
import net.koodar.suite.admin.module.system.permission.service.PermissionService;
import org.springframework.stereotype.Service;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.role.domain.RolePermission;

import java.util.Collection;
import java.util.List;

/**
 * Role permission service.
 *
 * @author liyc
 */
@Service
public class RolePermissionService {

	private final PermissionService permissionService;
	private final RolePermissionRepository rolePermissionRepository;

	public RolePermissionService(PermissionService permissionService, RolePermissionRepository rolePermissionRepository) {
		this.permissionService = permissionService;
		this.rolePermissionRepository = rolePermissionRepository;
	}

	public List<RolePermission> findByRoleId(Long roleId) {
		return rolePermissionRepository.findByRoleId(roleId);
	}

	public void deleteByRoleId(Long roleId) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
		rolePermissionRepository.deleteAll(rolePermissions);
	}

	public void saveOrUpdate(List<RolePermission> rolePermissionList) {
		rolePermissionRepository.saveAll(rolePermissionList);
	}

	public List<RolePermission> findByPermissionIdIn(Collection<Long> permissionIds) {
		return rolePermissionRepository.findByPermissionIdIn(permissionIds);
	}


	public void bindRoleWithUser(Long roleId, Collection<String> permissionNames) {

		// 需要绑定的所有权限
		List<Permission> permissions = permissionService.getPermissionsByName(permissionNames);
		List<Long> permissionIds = permissions.stream().map(Permission::getId).toList();

		// 用户已经绑定的权限
		List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
		List<Long> rolePermissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).toList();

		// 删除多余权限
		for (RolePermission rolePermission : rolePermissions) {
			if (!permissionIds.contains(rolePermission.getPermissionId())) {
				rolePermissionRepository.delete(rolePermission);
			}
		}

		// 增加缺少的权限
		for (Long permissionId : permissionIds) {
			if (!rolePermissionIds.contains(permissionId)) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setRoleId(roleId);
				rolePermission.setPermissionId(permissionId);
				rolePermissionRepository.save(rolePermission);
			}
		}

	}
}
