package net.koodar.suite.admin.module.system.user.manager;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.module.system.role.service.UserRoleService;
import net.koodar.suite.admin.module.system.user.domain.User;
import net.koodar.suite.admin.module.system.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 用户角色 manager
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class UserRoleManager {

	private final UserRepository userRepository;
	private final UserRoleService userRoleService;

	@Transactional(rollbackFor = Exception.class)
	public void updateUserRole(User user, Collection<Long> roleIds) {
		// 更新用户信息
		userRepository.save(user);
		// 更新用户角色信息
		userRoleService.bindRoleWithUser(user.getId(), roleIds);
	}

}
