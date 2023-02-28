package net.koodar.suite.admin.module.system.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.module.system.user.domain.*;
import net.koodar.suite.admin.module.system.user.service.UserService;
import net.koodar.suite.common.module.security.authentication.AppUserDetails;
import net.koodar.suite.common.module.operatelog.annoation.OperateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.admin.module.system.user.domain.PairVo;
import net.koodar.suite.admin.module.system.permission.service.PermissionService;
import net.koodar.suite.admin.module.system.role.service.RoleService;
import net.koodar.suite.admin.module.system.user.domain.assembler.UserAssembler;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * User controller
 *
 * @author liyc
 */
@OperateLog
@Tag(name = "系统管理-用户")
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
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

	/**
	 * 修改当前用户密码
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @return BaseResponse
	 */
	@PostMapping("/user/updatePassword")
	public BaseResponse<String> updatePassword(@RequestParam String oldPassword,
											   @RequestParam String newPassword) {
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.updatePassword(userDetails.getUserId(), oldPassword, newPassword);
		return BaseResponse.ok("用户密码修改成功");
	}

}
