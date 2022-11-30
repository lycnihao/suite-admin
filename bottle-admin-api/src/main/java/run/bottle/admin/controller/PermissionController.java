package run.bottle.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.model.vo.PermissionVo;
import run.bottle.admin.service.PermissionService;
import run.bottle.admin.service.assembler.PermissionAssembler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Permission controller
 *
 * @author liyc
 * @date 2022-11-27
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


}
