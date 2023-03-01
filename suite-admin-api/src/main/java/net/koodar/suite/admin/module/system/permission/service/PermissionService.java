package net.koodar.suite.admin.module.system.permission.service;
import java.util.*;

import net.koodar.suite.common.core.exception.ServiceException;
import net.koodar.suite.admin.module.system.permission.repository.PermissionRepository;
import net.koodar.suite.admin.module.system.role.repository.RolePermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.role.domain.RolePermission;
import net.koodar.suite.admin.module.system.permission.domain.PermissionTypeEnum;
import net.koodar.suite.admin.module.system.permission.domain.PermissionParam;

import java.util.stream.Collectors;

/**
 * Permission Service Impl
 *
 * @author liyc
 */
@Service
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final RolePermissionRepository rolePermissionRepository;

	public PermissionService(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
		this.permissionRepository = permissionRepository;
		this.rolePermissionRepository = rolePermissionRepository;
	}

	public List<Permission> listByRoleId(Long roleId) {
		return this.listByRoleIds(Collections.singleton(roleId));
	}

	public List<Permission> listByRoleIds(Collection<Long> roleIds) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleIdIn(roleIds);
		List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
		return permissionRepository.findAllById(permissionIds);
	}

	public List<Permission> getMenusByRoleIds(Collection<Long> roleIds) {
		List<Permission> permissions = this.listByRoleIds(roleIds);
		return permissions
				.stream()
				.filter(permission -> !permission.getType()
						.equals(PermissionTypeEnum.PERMISSIONS.getValue()))
				.collect(Collectors.toList());
	}

	public List<Permission> getMenusByAdmin() {
		List<Permission> permissions = this.getPermissions();
		return permissions
				.stream()
				.filter(permission -> !permission.getType()
						.equals(PermissionTypeEnum.PERMISSIONS.getValue()))
				.collect(Collectors.toList());
	}

	public List<Permission> getPermissions() {
		return permissionRepository.findAll();
	}

	public List<Permission> getPermissionsByType(PermissionTypeEnum permissionTypeEnum) {
		return permissionRepository.findByType(permissionTypeEnum.getValue());
	}

	public List<Permission> getPermissionsByName(Collection<String> names) {
		return permissionRepository.findByNameIn(names);
	}

	public void savePermission(PermissionParam permissionParam) {
		Long parenId = 0L;
		if (PermissionTypeEnum.MENU.getValue() != permissionParam.getType() &&
				StringUtils.hasLength(permissionParam.getParentKey())) {
			Optional<Permission> parentOptional = permissionRepository.findByName(permissionParam.getParentKey());
			if (!parentOptional.isPresent()) {
				throw new ServiceException("父节点不存在");
			}
			parenId = parentOptional.get().getId();
		}

		if (!Objects.isNull(permissionParam.getId())) {
			Optional<Permission> dbPermissionOptional = permissionRepository.findByName(permissionParam.getName());
			if (dbPermissionOptional.isPresent()) {
				Permission permission = dbPermissionOptional.get();
				if (!permission.getId().equals(permissionParam.getId())) {
					throw new ServiceException("权限已存在");
				}
			}
		}

		Permission permission = new Permission();
		permission.setParentId(parenId);
		permission.setId(permissionParam.getId());
		permission.setName(permissionParam.getName());
		permission.setTitle(permissionParam.getTitle());
		permission.setType(permissionParam.getType());
		permission.setIcon(permissionParam.getIcon());
		permission.setPath(permissionParam.getPath());
		permission.setRedirect(permissionParam.getRedirect());
		permission.setComponent(permissionParam.getComponent());
		permission.setSort(permissionParam.getSort());
		permission.setKeepAlive(permissionParam.getKeepAlive());
		permissionRepository.save(permission);
	}

	public void deletePermission(Long id) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findByPermissionId(id);
		if (rolePermissions.isEmpty()) {
			permissionRepository.deleteById(id);
		} else {
			throw new ServiceException("当前菜单权限已存在绑定的角色，请解除绑定后再试");
		}
	}
}
