package net.koodar.suite.admin.module.system.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.permission.domain.PermissionParam;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.admin.module.system.permission.domain.PermissionVo;
import net.koodar.suite.admin.module.system.permission.service.PermissionService;
import net.koodar.suite.admin.module.system.permission.domain.assembler.PermissionAssembler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission controller
 *
 * @author liyc
 */
@Tag(name = "系统管理-权限")
@RestController
public class PermissionController {

	private final PermissionService permissionService;
	private final PermissionAssembler permissionAssembler;

	public PermissionController(PermissionService permissionService, PermissionAssembler permissionAssembler) {
		this.permissionService = permissionService;
		this.permissionAssembler = permissionAssembler;
	}

	@Operation(summary = "获取所有权限")
	@GetMapping("/permission/getAllPermissions")
	public BaseResponse<List<PermissionVo>> listAllPermission() {
		List<Permission> permissions = permissionService.getPermissions();
		List<Permission> parentPermissions = permissions.stream().filter(permission -> permission.getParentId() != null && permission.getParentId() <= 0).collect(Collectors.toList());
		return BaseResponse.ok(permissionAssembler.toPermissionTree(parentPermissions, permissions));
	}

	/**
	 * 添加权限
	 *
	 * @param permissionParam 权限参数
	 * @return 操作结果
	 */
	@Operation(summary = "添加权限")
	@PostMapping("/permission/add")
	public BaseResponse<String> addPermission(@RequestBody PermissionParam permissionParam) {
		permissionService.savePermission(permissionParam);
		return BaseResponse.ok("添加成功");
	}

	/**
	 * 更新权限
	 *
	 * @param permissionParam 权限参数
	 * @return 操作结果
	 */
	@Operation(summary = "更新权限")
	@PostMapping("/permission/update")
	public BaseResponse<String> updatePermission(@RequestBody PermissionParam permissionParam) {
		permissionService.savePermission(permissionParam);
		return BaseResponse.ok("更新成功");
	}

	/**
	 * 删除权限
	 *
	 * @param permissionId 权限Id
	 * @return 操作结果
	 */
	@Operation(summary = "删除权限")
	@PostMapping("/permission/delete")
	public BaseResponse<String> deletedPermission(@RequestParam Long permissionId) {
		permissionService.deletePermission(permissionId);
		return BaseResponse.ok("删除成功");
	}

}
