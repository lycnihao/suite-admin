package run.bottle.admin.service.impl;

import org.springframework.stereotype.Service;
import run.bottle.admin.model.entity.Role;
import run.bottle.admin.model.entity.UserRole;
import run.bottle.admin.repository.RoleRepository;
import run.bottle.admin.repository.UserRoleRepository;
import run.bottle.admin.service.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Role Service Impl.
 *
 * @author liyc
 * @date 2022-11-09
 */
@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;

	public RoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public List<Role> findListByIds(Collection<Long> ids) {
		return roleRepository.findAllByIdIn(ids);
	}

	@Override
	public List<Role> findListByUserIds(Collection<Long> userIds) {
		List<UserRole> userRoles = userRoleRepository.findAllByUserIdIn(userIds);
		List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
		return roleRepository.findAllByIdIn(roleIds);
	}

	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findByStatus(1);
	}
}
