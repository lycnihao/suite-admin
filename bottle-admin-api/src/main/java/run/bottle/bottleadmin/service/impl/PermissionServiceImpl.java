package run.bottle.bottleadmin.service.impl;

import org.springframework.stereotype.Service;
import run.bottle.bottleadmin.model.entity.Permission;
import run.bottle.bottleadmin.model.entity.RolePermission;
import run.bottle.bottleadmin.repository.PermissionRepository;
import run.bottle.bottleadmin.repository.RolePermissionRepository;
import run.bottle.bottleadmin.service.PermissionService;

import java.util.List;
import java.util.Set;
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
	public List<Permission> listByRoleIds(List<Long> roleIds) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleIdIn(roleIds);
		List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
		return permissionRepository.findAllById(permissionIds);
	}
}
