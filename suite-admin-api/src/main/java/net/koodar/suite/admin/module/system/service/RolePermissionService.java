package net.koodar.suite.admin.module.system.service;

import net.koodar.suite.admin.module.system.model.entity.RolePermission;

import java.util.Collection;
import java.util.List;

/**
 * Role permission service.
 *
 * @author liyc
 */
public interface RolePermissionService {

	List<RolePermission> findByRoleId(Long roleId);

	void deleteByRoleId(Long roleId);

	void saveOrUpdate(List<RolePermission> rolePermissionList);

	void bindRoleWithUser(Long roleId, Collection<String> permissionNames);

}
