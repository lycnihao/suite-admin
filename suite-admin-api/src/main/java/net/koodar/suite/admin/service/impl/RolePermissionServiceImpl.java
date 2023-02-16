package net.koodar.suite.admin.service.impl;

import net.koodar.suite.admin.repository.RolePermissionRepository;
import net.koodar.suite.admin.service.PermissionService;
import net.koodar.suite.admin.service.RolePermissionService;
import org.springframework.stereotype.Service;
import net.koodar.suite.admin.model.entity.Permission;
import net.koodar.suite.admin.model.entity.RolePermission;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Role permission service impl.
 *
 * @author liyc
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

	private final PermissionService permissionService;
	private final RolePermissionRepository rolePermissionRepository;

	public RolePermissionServiceImpl(PermissionService permissionService, RolePermissionRepository rolePermissionRepository) {
		this.permissionService = permissionService;
		this.rolePermissionRepository = rolePermissionRepository;
	}

	@Override
	public List<RolePermission> findByRoleId(Long roleId) {
		return rolePermissionRepository.findByRoleId(roleId);
	}

	@Override
	public void deleteByRoleId(Long roleId) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
		rolePermissionRepository.deleteAll(rolePermissions);
	}

	@Override
	public void saveOrUpdate(List<RolePermission> rolePermissionList) {
		rolePermissionRepository.saveAll(rolePermissionList);
	}

	@Override
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
