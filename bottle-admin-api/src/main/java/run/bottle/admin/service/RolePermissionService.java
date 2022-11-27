package run.bottle.admin.service;

import run.bottle.admin.model.entity.RolePermission;

import java.util.Collection;
import java.util.List;

/**
 * Role permission service.
 *
 * @author liyc
 * @date 2022-11-27
 */
public interface RolePermissionService {

	List<RolePermission> findByRoleId(Long roleId);

	void deleteByRoleId(Long roleId);

	void saveOrUpdate(List<RolePermission> rolePermissionList);

	void bindRoleWithUser(Long roleId, Collection<String> permissionNames);

}
