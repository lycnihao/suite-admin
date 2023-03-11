package net.koodar.suite.admin.service.assembler;

import net.koodar.suite.admin.model.entity.User;
import net.koodar.suite.admin.model.vo.UserVo;
import net.koodar.suite.common.exception.ServiceException;
import net.koodar.suite.admin.service.RoleService;
import net.koodar.suite.admin.service.UserRoleService;
import net.koodar.suite.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import net.koodar.suite.admin.model.entity.Role;
import net.koodar.suite.admin.model.entity.UserRole;
import net.koodar.suite.admin.model.vo.UserListVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User assembler.
 *
 * @author liyc
 */
@Component
public class UserAssembler {

	private final RoleService roleService;
	private final UserRoleService userRoleService;

	public UserAssembler(UserService userService, RoleService roleService, UserRoleService userRoleService) {
		this.roleService = roleService;
		this.userRoleService = userRoleService;
	}

	public Page<UserListVo> convertToListVo(Page<User> userPage) {
		Assert.notNull(userPage, "User page must not be null");

		List<User> users = userPage.getContent();
		List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
		List<UserRole> userRoles = userRoleService.findAllByUserIds(userIds);
		List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
		List<Role> roles = roleService.findListByIds(roleIds);
		Map<Long, String> roleNameMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getCode));
		Map<Long, List<UserRole>> userRolesMap = userRoles.stream().collect(Collectors.groupingBy(UserRole::getUserId));

		return userPage.map(user -> {
			UserListVo userVo = new UserListVo();
			userVo.setUserId(user.getId());
			userVo.setUsername(user.getUsername());
			userVo.setNickname(user.getNickname());
			userVo.setEmail(user.getEmail());
			userVo.setAvatar(user.getAvatar());
			userVo.setCreateDate(user.getCreateTime());

			List<UserRole> userRoleList = userRolesMap.get(user.getId());
			if (userRoleList.isEmpty()) {
				throw new ServiceException("User permission is empty");
			}

			// Set roles
			userVo.setRoles(userRoleList
					.stream()
					.map(userRole -> roleNameMap.get(userRole.getRoleId()))
					.collect(Collectors.toSet()));
			return userVo;
		});
	}

	public UserVo convertToUserVo(User user) {
		UserVo userVo = new UserVo();
		userVo.setUserId(user.getId());
		userVo.setUsername(user.getUsername());
		userVo.setNickname(user.getNickname());
		userVo.setEmail(user.getEmail());
		userVo.setAvatar(user.getAvatar());
		return userVo;
	}
}
