package run.bottle.admin.service;

import run.bottle.admin.model.entity.Role;

import java.util.Collection;
import java.util.List;

/**
 * Role Service.
 *
 * @author liyc
 * @date 2022-11-09
 */
public interface RoleService {

	List<Role> findListByIds(Collection<Long> ids);

	List<Role> findListByUserIds(Collection<Long> userIds);

	List<Role> findAllRoles();

}
