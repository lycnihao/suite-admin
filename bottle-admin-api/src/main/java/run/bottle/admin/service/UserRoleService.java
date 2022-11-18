package run.bottle.admin.service;

import run.bottle.admin.model.entity.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * @author liyc
 * @date 2022-11-10
 */
public interface UserRoleService {

	List<UserRole> findAllByUserIds(Collection<Long> userIds);

	void bindRoleWithUser(Long userId, Collection<Long> roleId);

	void saveOrUpdate(List<UserRole> userRoles);

}
