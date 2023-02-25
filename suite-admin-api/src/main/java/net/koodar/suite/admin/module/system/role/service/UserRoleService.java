package net.koodar.suite.admin.module.system.role.service;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.module.system.role.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import net.koodar.suite.admin.module.system.role.domain.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User role service.
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class UserRoleService {

	private final UserRoleRepository userRoleRepository;


	public List<UserRole> findAllByUserIds(Collection<Long> userIds) {
		return userRoleRepository.findAllByUserIdIn(userIds);
	}

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
				.toList();

		// 新增角色
		List<Long> missingRoles = roleIds.stream()
				.filter(roleId -> !userRoleIds.contains(roleId))
				.toList();

		// 组装UserRole
		List<UserRole> saveRoles = missingRoles.stream().map(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			return userRole;
		}).collect(Collectors.toList());

		userRoleRepository.saveAll(saveRoles);

	}

	public void saveOrUpdate(List<UserRole> userRoles) {
		userRoleRepository.saveAll(userRoles);
	}
}
