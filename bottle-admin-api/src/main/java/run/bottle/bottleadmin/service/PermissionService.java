package run.bottle.bottleadmin.service;

import run.bottle.bottleadmin.model.entity.Permission;
import run.bottle.bottleadmin.model.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * Permission Service.
 *
 * @author liyc
 * @date 2022-09-06
 */
public interface PermissionService {

	List<Permission> listByRoleIds(List<Long> roleIds);

}
