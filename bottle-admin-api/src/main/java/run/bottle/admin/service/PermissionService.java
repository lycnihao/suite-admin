package run.bottle.admin.service;

import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.enums.PermissionTypeEnum;
import run.bottle.admin.model.params.PermissionParam;

import java.util.Collection;
import java.util.List;

/**
 * Permission Service.
 *
 * @author liyc
 * @date 2022-09-06
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
