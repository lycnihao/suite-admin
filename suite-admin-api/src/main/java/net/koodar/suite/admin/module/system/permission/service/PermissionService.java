package net.koodar.suite.admin.module.system.permission.service;
import java.util.*;

import net.koodar.suite.common.core.exception.ServiceException;
import net.koodar.suite.admin.module.system.permission.repository.PermissionRepository;
import net.koodar.suite.admin.module.system.role.repository.RolePermissionRepository;
import net.koodar.suite.common.support.datatracer.DataTracerService;
import net.koodar.suite.common.support.datatracer.domain.DataTracer;
import net.koodar.suite.common.support.security.authorization.DynamicSecurityMetadataSource;
import net.koodar.suite.common.util.BeanUtil;
import org.springframework.context.ApplicationContext;
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
	private final ApplicationContext applicationContext;

	private final DataTracerService dataTracerService;

	public PermissionService(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository, ApplicationContext applicationContext, DataTracerService dataTracerService) {
		this.permissionRepository = permissionRepository;
		this.rolePermissionRepository = rolePermissionRepository;
		this.applicationContext = applicationContext;
		this.dataTracerService = dataTracerService;
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
			if (parentOptional.isEmpty()) {
				throw new ServiceException("??????????????????");
			}
			parenId = parentOptional.get().getId();
		}

		if (!Objects.isNull(permissionParam.getId())) {
			Optional<Permission> dbPermissionOptional = permissionRepository.findByName(permissionParam.getName());
			if (dbPermissionOptional.isPresent()) {
				Permission permission = dbPermissionOptional.get();
				if (!permission.getId().equals(permissionParam.getId())) {
					throw new ServiceException("???????????????");
				}
			}
		}

		Permission permission = new Permission();
		BeanUtil.copy(permissionParam, permission);
		permission.setParentId(parenId);
		permissionRepository.save(permission);

		// ????????????
		DynamicSecurityMetadataSource dynamicSecurityMetadataSource = (DynamicSecurityMetadataSource) applicationContext.getBean("dynamicSecurityMetadataSource");
		dynamicSecurityMetadataSource.loadDataSource();
	}

	public void deletePermission(Long id) {
		List<RolePermission> rolePermissions = rolePermissionRepository.findByPermissionId(id);
		if (rolePermissions.isEmpty()) {
			permissionRepository.deleteById(id);
		} else {
			throw new ServiceException("?????????????????????????????????????????????????????????????????????");
		}
	}
}
