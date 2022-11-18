package run.bottle.admin.service.impl;

import org.springframework.stereotype.Service;
import run.bottle.admin.model.entity.UserRole;
import run.bottle.admin.repository.UserRoleRepository;
import run.bottle.admin.service.UserRoleService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User role service.
 *
 * @author liyc
 * @date 2022-11-10
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

	private final UserRoleRepository userRoleRepository;

	public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}


	@Override
	public List<UserRole> findAllByUserIds(Collection<Long> userIds) {
		return userRoleRepository.findAllByUserIdIn(userIds);
	}

	@Override
	public void bindRoleWithUser(Long userId, Collection<Long> roleIds) {
		List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);

		// 删除角色
		List<UserRole> extraRoles = userRoles.stream()
				.filter(userRole -> !roleIds.contains(userRole.getRoleId()))
				.collect(Collectors.toList());
		userRoleRepository.deleteAll(extraRoles);

		// 用户角色
		List<Long> userRoleIds = userRoles.stream()
				.map(UserRole::getRoleId)
				.filter(roleId -> extraRoles.stream().noneMatch(extraRole -> extraRole.getRoleId().equals(roleId)))
				.collect(Collectors.toList());

		// 新增角色
		List<Long> missingRoles = roleIds.stream()
				.filter(roleId -> !userRoleIds.contains(roleId))
				.collect(Collectors.toList());

		// 组装UserRole
		List<UserRole> saveRoles = missingRoles.stream().map(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			return userRole;
		}).collect(Collectors.toList());

		userRoleRepository.saveAll(saveRoles);

	}

	@Override
	public void saveOrUpdate(List<UserRole> userRoles) {
		userRoleRepository.saveAll(userRoles);
	}
}
