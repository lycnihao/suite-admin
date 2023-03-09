package net.koodar.suite.admin.module.system.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.module.system.user.domain.*;
import net.koodar.suite.admin.module.system.user.service.UserService;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.support.operatelog.annoation.OperateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.common.core.support.BaseResponse;
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
@Tag(name = "UserController", description = "系统管理-用户")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer")
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
	 * 获取登录用户的用户信息
	 *
	 * @return 用户信息
	 */
	@Operation(summary = "获取登录用户的用户信息")
	@GetMapping("/user/info")
	public BaseResponse<UserVo> getUserInfo() {
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = userService.loadUserById(userDetails.getUserId());
		UserVo userVo = userAssembler.convertToUserVo(user);

		List<Permission> permissions = userDetails.getAdministratorFlag() ?
				permissionService.getPermissions() : permissionService.listByRoleIds(userDetails.getRoleIds());
		userVo.setPermissions(permissions
				.stream()
				.map(permission -> Map.of(
						"label", permission.getName(),
						"value", permission.getName()))
				.collect(Collectors.toSet()));
		return BaseResponse.ok(userVo);
	}

	/**
	 * 通过userId获取用户信息
	 *
	 * @param userId 用户id
	 * @return 用户信息
	 */
	@Operation(summary = "通过userId获取用户信息")
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
	 * 获取用户列表
	 *
	 * @param pageable 分页参数
	 * @param userQuery 查询条件
	 * @return 用户列表
	 */
	@Operation(summary = "获取用户列表")
	@GetMapping("/user/list")
	public Page<UserListVo> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = DESC) Pageable pageable,
			UserQuery userQuery) {
		return userAssembler.convertToListVo(userService.pageBy(userQuery, pageable));
	}

	/**
	 * 添加用户
	 *
	 * @param userParam 用户参数
	 * @return 操作结果
	 */
	@Operation(summary = "添加用户")
	@PostMapping("/user/add")
	public BaseResponse<String> addUser(@RequestBody UserParam userParam) {
		userService.addUser(userParam);
		return BaseResponse.ok("用户添加成功");
	}

	/**
	 * 更新用户
	 *
	 * @param userParam 用户参数
	 * @return 操作结果
	 */
	@Operation(summary = "更新用户")
	@PostMapping("/user/update")
	public BaseResponse<String> updateUser(@RequestBody UserParam userParam) {
		userService.updateUser(userParam);
		return BaseResponse.ok("用户更新成功");
	}

	/**
	 * 删除用户
	 *
	 * @param userId 用户Id
	 * @return 操作结果
	 */
	@Operation(summary = "删除用户")
	@PostMapping("/user/delete")
	public BaseResponse<String> deletedUser(@RequestParam Long userId) {
		userService.deleteUser(userId);
		return BaseResponse.ok("用户删除成功");
	}

	/**
	 * 修改当前用户密码
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @return 操作结果
	 */
	@Operation(summary = "修改当前用户密码")
	@PostMapping("/user/updatePassword")
	public BaseResponse<String> updatePassword(@RequestParam String oldPassword,
											   @RequestParam String newPassword) {
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.updatePassword(userDetails.getUserId(), oldPassword, newPassword);
		return BaseResponse.ok("用户密码修改成功");
	}

}
