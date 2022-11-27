package run.bottle.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import run.bottle.admin.model.entity.Role;
import run.bottle.admin.model.params.RoleParam;
import run.bottle.admin.model.params.RoleQuery;

import java.util.Collection;
import java.util.List;

/**
 * Role Service.
 *
 * @author liyc
 * @date 2022-11-09
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
