package net.koodar.suite.admin.module.system.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.admin.module.system.role.domain.RoleParam;
import net.koodar.suite.admin.module.system.role.domain.RoleQuery;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.admin.module.system.role.domain.RoleVo;
import net.koodar.suite.admin.module.system.permission.service.PermissionService;
import net.koodar.suite.admin.module.system.role.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Role controller.
 *
 * @author liyc
 */
@Tag(name = "RoleController", description = "系统管理-角色")
@RestController
public class RoleController {

	private final RoleService roleService;
	private final PermissionService permissionService;

	public RoleController(RoleService roleService, PermissionService permissionService) {
		this.roleService = roleService;
		this.permissionService = permissionService;
	}

	/**
	 * 获取所有角色列表
	 * @return 角色列表
	 */
	@Operation(summary = "获取所有角色列表")
	@GetMapping("/role/getAllRoles")
	public BaseResponse<List<Role>> getRoles() {
		return BaseResponse.ok(roleService.findAllRoles());
	}

	/**
	 * 查询角色列表
	 * @param pageable 分页参数
	 * @param roleQuery 查询条件
	 * @return 角色列表
	 */
	@Operation(summary = "查询角色列表")
	@GetMapping("/role/list")
	public Page<Role> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = DESC) Pageable pageable,
			RoleQuery roleQuery) {
		return roleService.pageBy(roleQuery, pageable);
	}

	/**
	 * 获取角色信息
	 * @param id 角色id
	 * @return 角色信息
	 */
	@Operation(summary = "获取角色信息")
	@GetMapping("/role/info")
	public BaseResponse<RoleVo> getRoleInfo(@RequestParam Long id) {
		RoleVo roleVo = new RoleVo();
		Role role = roleService.findById(id);
		List<Permission> permissions = permissionService.listByRoleId(id);
		List<String> rolePermissions = permissions.stream().map(Permission::getName).collect(Collectors.toList());
		roleVo.setRoleId(role.getId());
		roleVo.setCode(role.getCode());
		roleVo.setName(role.getName());
		roleVo.setDescription(role.getDescription());
		roleVo.setPermissions(rolePermissions);
		return BaseResponse.ok(roleVo);
	}

	/**
	 * 添加角色
	 *
	 * @param roleParam 角色参数
	 * @return 操作结果
	 */
	@Operation(summary = "添加角色")
	@PostMapping("/role/add")
	public BaseResponse<String> addRole(@RequestBody RoleParam roleParam) {
		roleService.addRole(roleParam);
		return BaseResponse.ok("角色添加成功");
	}

	/**
	 * 更新角色
	 *
	 * @param roleParam 角色参数
	 * @return 操作结果
	 */
	@Operation(summary = "更新角色")
	@PostMapping("/role/update")
	public BaseResponse<String> updateRole(@RequestBody RoleParam roleParam) {
		roleService.updateRole(roleParam);
		return BaseResponse.ok("角色更新成功");
	}

	/**
	 * 删除角色
	 *
	 * @param roleId 角色Id
	 * @return 操作结果
	 */
	@Operation(summary = "删除角色")
	@PostMapping("/role/delete")
	public BaseResponse<String> deleteRole(@RequestParam Long roleId) {
		roleService.deleteRole(roleId);
		return BaseResponse.ok("角色删除成功");
	}
}
