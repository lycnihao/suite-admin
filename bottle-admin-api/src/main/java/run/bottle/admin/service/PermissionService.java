package run.bottle.admin.service;

import run.bottle.admin.model.entity.Permission;

import java.util.Collection;
import java.util.List;

/**
 * Permission Service.
 *
 * @author liyc
 * @date 2022-09-06
 */
public interface PermissionService {

	List<Permission> listByRoleIds(Collection<Long> roleIds);

	List<Permission> getMenusByRoleIds(Collection<Long> roleIds);

}
