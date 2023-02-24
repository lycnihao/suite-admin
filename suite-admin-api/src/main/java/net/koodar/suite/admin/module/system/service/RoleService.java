package net.koodar.suite.admin.module.system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import net.koodar.suite.admin.module.system.model.entity.Role;
import net.koodar.suite.admin.module.system.model.params.RoleParam;
import net.koodar.suite.admin.module.system.model.params.RoleQuery;

import java.util.Collection;
import java.util.List;

/**
 * Role Service.
 *
 * @author liyc
 */
public interface RoleService {

	Role findById(Long id);

	List<Role> findListByIds(Collection<Long> ids);

	List<Role> findListByUserIds(Collection<Long> userIds);

	List<Role> findAllRoles();

	Page<Role> pageBy(RoleQuery roleQuery, Pageable pageable);

	void addRole(RoleParam roleParam);

	void updateRole(RoleParam roleParam);

	void deleteRole(Long id);

}
