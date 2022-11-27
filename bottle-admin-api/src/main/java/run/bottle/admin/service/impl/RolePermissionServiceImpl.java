package run.bottle.admin.service.impl;

import org.springframework.stereotype.Service;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.entity.RolePermission;
import run.bottle.admin.repository.RolePermissionRepository;
import run.bottle.admin.service.PermissionService;
import run.bottle.admin.service.RolePermissionService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Role permission service impl.
 *
 * @author liyc
 * @date 2022-11-27
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

		List<Permission> permissions = permissionService.getPermissionsByName(permissionNames);
		List<Long> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());

		List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);

		// 需要删除的权限
		List<RolePermission> extraPermissions = rolePermissions.stream()
				.filter(rolePermission -> !permissionIds.contains(rolePermission.getPermissionId()))
				.collect(Collectors.toList());
		rolePermissionRepository.deleteAll(extraPermissions);

		// 角色权限
		List<Long> userPermissionIds = permissionIds.stream()
				.filter(permissionId -> extraPermissions.stream()
						.noneMatch(extraRole -> extraRole.getPermissionId().equals(permissionId)))
				.collect(Collectors.toList());

		// 需要新增的权限
		List<Long> missingPermissions = permissionIds.stream()
				.filter(permissionId -> !userPermissionIds.contains(permissionId))
				.collect(Collectors.toList());

		// 组装RolePermission
		List<RolePermission> savePermissions = missingPermissions.stream().map(permissionId -> {
			RolePermission rolePermission = new RolePermission();
			rolePermission.setRoleId(roleId);
			rolePermission.setPermissionId(permissionId);
			return rolePermission;
		}).collect(Collectors.toList());

		rolePermissionRepository.saveAll(savePermissions);
	}
}
