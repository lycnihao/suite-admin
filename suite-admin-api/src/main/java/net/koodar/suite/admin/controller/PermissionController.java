package net.koodar.suite.admin.controller;

import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.model.entity.Permission;
import net.koodar.suite.admin.model.params.PermissionParam;
import net.koodar.suite.admin.model.support.BaseResponse;
import net.koodar.suite.admin.model.vo.PermissionVo;
import net.koodar.suite.admin.service.PermissionService;
import net.koodar.suite.admin.service.assembler.PermissionAssembler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission controller
 *
 * @author liyc
 */
@RestController
public class PermissionController {

	private final PermissionService permissionService;
	private final PermissionAssembler permissionAssembler;

	public PermissionController(PermissionService permissionService, PermissionAssembler permissionAssembler) {
		this.permissionService = permissionService;
		this.permissionAssembler = permissionAssembler;
	}

	@GetMapping("/permission/getAllPermissions")
	public BaseResponse<List<PermissionVo>> listAllPermission() {
		List<Permission> permissions = permissionService.getPermissions();
		List<Permission> parentPermissions = permissions.stream().filter(permission -> permission.getParentId() != null && permission.getParentId() <= 0).collect(Collectors.toList());
		return BaseResponse.ok(permissionAssembler.toPermissionTree(parentPermissions, permissions));
	}

	/**
	 * Add a permission
	 *
	 * @param permissionParam Param
	 * @return Add result
	 */
	@PostMapping("/permission/add")
	public BaseResponse<String> addPermission(PermissionParam permissionParam) {
		permissionService.savePermission(permissionParam);
		return BaseResponse.ok("添加成功");
	}

	/**
	 * Update a permission
	 *
	 * @param permissionParam Param
	 * @return Update result
	 */
	@PostMapping("/permission/update")
	public BaseResponse<String> updatePermission(PermissionParam permissionParam) {
		permissionService.savePermission(permissionParam);
		return BaseResponse.ok("更新成功");
	}

	/**
	 * Delete a permission
	 *
	 * @param permissionId permission id
	 * @return Update result
	 */
	@PostMapping("/permission/delete")
	public BaseResponse<String> deletedPermission(@RequestParam Long permissionId) {
		permissionService.deletePermission(permissionId);
		return BaseResponse.ok("删除成功");
	}

}