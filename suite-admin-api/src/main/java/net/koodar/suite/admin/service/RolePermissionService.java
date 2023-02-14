package net.koodar.suite.admin.service;

import net.koodar.suite.admin.model.entity.RolePermission;

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
