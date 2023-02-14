package net.koodar.suite.admin.service;

import net.koodar.suite.admin.model.entity.Permission;
import net.koodar.suite.admin.model.enums.PermissionTypeEnum;
import net.koodar.suite.admin.model.params.PermissionParam;

import java.util.Collection;
import java.util.List;

/**
 * Permission Service.
 *
 * @author liyc
 */
public interface PermissionService {

	List<Permission> listByRoleId(Long roleId);

	List<Permission> listByRoleIds(Collection<Long> roleIds);

	List<Permission> getMenusByRoleIds(Collection<Long> roleIds);

	List<Permission> getPermissions();

	List<Permission> getPermissionsByType(PermissionTypeEnum permissionTypeEnum);

	List<Permission> getPermissionsByName(Collection<String> names);

	void savePermission(PermissionParam permissionParam);

	void deletePermission(Long id);

}