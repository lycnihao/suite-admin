package net.koodar.suite.admin.controller;

import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.security.support.AppUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.model.entity.Permission;
import net.koodar.suite.admin.model.entity.Role;
import net.koodar.suite.admin.model.entity.User;
import net.koodar.suite.admin.model.params.UserParam;
import net.koodar.suite.admin.model.params.UserQuery;
import net.koodar.suite.admin.model.support.BaseResponse;
import net.koodar.suite.admin.model.vo.PairVo;
import net.koodar.suite.admin.model.vo.UserListVo;
import net.koodar.suite.admin.model.vo.UserVo;
import net.koodar.suite.admin.security.util.SecurityContextUtils;
import net.koodar.suite.admin.service.PermissionService;
import net.koodar.suite.admin.service.RoleService;
import net.koodar.suite.admin.service.UserService;
import net.koodar.suite.admin.service.assembler.UserAssembler;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * User controller
 *
 * @author liyc
 */
@Slf4j
@RestController
public class UserController {

	private final UserService userService;
	private final RoleService roleService;
	private final PermissionService permissionService;
	private final UserAssembler userAssembler;

	public UserController(UserService userService, RoleService roleService, PermissionService permissionService, UserAssembler userAssembler) {
		this.userService = userService;
		this.roleService = roleService;
		this.permissionService = permissionService;
		this.userAssembler = userAssembler;
	}

	/**
	 * Get user info of login user
	 *
	 * @return User info
	 */
	@GetMapping("/user/info")
	public BaseResponse<UserVo> getUserInfo() {
		AppUserDetails userDetails = SecurityContextUtils.getUserDetails();

		User user = userService.loadUserById(userDetails.getUserId());
		UserVo userVo = userAssembler.convertToUserVo(user);

		List<Permission> permissions = permissionService.listByRoleIds(userDetails.getRoleIds());
		userVo.setPermissions(permissions
				.stream()
				.map(permission -> PairVo.of(permission.getTitle(), permission.getName()))
				.collect(Collectors.toSet()));
		return BaseResponse.ok(userVo);
	}

	/**
	 * Get user info by userId
	 *
	 * @param userId User id
	 * @return User info
	 */
	@GetMapping(value="/user/info", params={"userId"})
	public BaseResponse<UserVo> getUserInfoById(@RequestParam Long userId) {
		User user = userService.loadUserById(userId);
		UserVo userVo = userAssembler.convertToUserVo(user);
		List<Role> roles = roleService.findListByUserIds(Collections.singletonList(userId));
		Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
		userVo.setRoleIds(roleIds);
		return BaseResponse.ok(userVo);
	}

	/**
	 * Get list of user
	 *
	 * @param pageable Pagination param
	 * @param userQuery Search param
	 * @return A list of user
	 */
	@GetMapping("/user/list")
	public Page<UserListVo> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = DESC) Pageable pageable,
			UserQuery userQuery) {
		return userAssembler.convertToListVo(userService.pageBy(userQuery, pageable));
	}

	/**
	 * Add a user
	 *
	 * @param userParam Param
	 * @return Add result
	 */
	@PostMapping("/user/add")
	public BaseResponse<String> addUser(@RequestBody UserParam userParam) {
		userService.addUser(userParam);
		return BaseResponse.ok("用户添加成功");
	}

	/**
	 * Update a user
	 *
	 * @param userParam Param
	 * @return Update result
	 */
	@PostMapping("/user/update")
	public BaseResponse<String> updateUser(@RequestBody UserParam userParam) {
		userService.updateUser(userParam);
		return BaseResponse.ok("用户更新成功");
	}

	/**
	 * Delete a user
	 *
	 * @param userId user id
	 * @return Update result
	 */
	@PostMapping("/user/delete")
	public BaseResponse<String> deletedUser(@RequestParam Long userId) {
		userService.deleteUser(userId);
		return BaseResponse.ok("用户删除成功");
	}

}
