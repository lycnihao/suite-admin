package net.koodar.suite.admin.module.system.user.manager;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.admin.module.system.role.repository.RoleRepository;
import net.koodar.suite.admin.module.system.role.service.UserRoleService;
import net.koodar.suite.admin.module.system.user.domain.User;
import net.koodar.suite.admin.module.system.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户角色 manager
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class UserRoleManager {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleService userRoleService;
	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public void updateUserRole(User user, Collection<Long> roleIds) {
		// 更新用户信息
		userRepository.save(user);
		// 更新用户角色信息
		userRoleService.bindRoleWithUser(user.getId(), roleIds);
	}

	public void createAdministrator(String username, String password, String roleName) {
		User user = new User();
		user.setUsername(username);
		user.setNickname(username);
		user.setEmail("30707145@qq.com");
		user.setAvatar(null);
		user.setDescription("超级管理员");
		user.setPassword(passwordEncoder.encode(password));
		user.setAdministratorFlag(false);
		userRepository.save(user);
		Role role = new Role();
		role.setName(roleName);
		role.setCode(roleName);
		role.setDescription("超级管理员");
		roleRepository.save(role);
		userRoleService.bindRoleWithUser(user.getId(), Collections.singletonList(role.getId()));
	}


}
