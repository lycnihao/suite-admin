package run.bottle.admin.service.impl;

import org.springframework.stereotype.Service;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.entity.RolePermission;
import run.bottle.admin.model.enums.PermissionTypeEnum;
import run.bottle.admin.repository.PermissionRepository;
import run.bottle.admin.repository.RolePermissionRepository;
import run.bottle.admin.service.PermissionService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission Service Impl
 *
 * @author liyc
 * @date 2022-09-06
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;
	private final RolePermissionRepository rolePermissionRepository;

	public PermissionServiceImpl(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
		this.permissionRepository = permissionRepository;
		this.rolePermissionRepository = rolePermissionRepository;
	}

	@Override
	public List<Permission> listByRoleId(Long roleId) {
		return this.listByRoleIds(Collections.singleton(roleId));
	}

	@Override
	public List<Permission> listByRoleIds(Collection<Long> roleIds) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleIdIn(roleIds);
		List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
		return permissionRepository.findAllById(permissionIds);
	}

	@Override
	public List<Permission> getMenusByRoleIds(Collection<Long> roleIds) {
		List<Permission> permissions = this.listByRoleIds(roleIds);
		return permissions
				.stream()
				.filter(permission -> !permission.getType()
						.equals(PermissionTypeEnum.PERMISSIONS.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Permission> getPermissions() {
		return permissionRepository.findAll();
	}

	@Override
	public List<Permission> getPermissionsByName(Collection<String> names) {
		return permissionRepository.findByNameIn(names);
	}
}
