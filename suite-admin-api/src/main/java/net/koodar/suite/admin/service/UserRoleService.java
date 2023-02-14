package net.koodar.suite.admin.service;

import net.koodar.suite.admin.model.entity.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * @author liyc
 */
public interface UserRoleService {

	List<UserRole> findAllByUserIds(Collection<Long> userIds);

	void bindRoleWithUser(Long userId, Collection<Long> roleId);

	void saveOrUpdate(List<UserRole> userRoles);

}
