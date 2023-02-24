package net.koodar.suite.admin.module.system.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import net.koodar.suite.admin.module.system.model.entity.Permission;
import net.koodar.suite.admin.module.system.model.entity.Role;
import net.koodar.suite.admin.module.system.model.params.RoleParam;
import net.koodar.suite.admin.module.system.model.params.RoleQuery;
import net.koodar.suite.admin.module.system.model.support.BaseResponse;
import net.koodar.suite.admin.module.system.model.vo.RoleVo;
import net.koodar.suite.admin.module.system.service.PermissionService;
import net.koodar.suite.admin.module.system.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Role controller.
 *
 * @author liyc
 */
@RestController
public class RoleController {

	private final RoleService roleService;
	private final PermissionService permissionService;

	public RoleController(RoleService roleService, PermissionService permissionService) {
		this.roleService = roleService;
		this.permissionService = permissionService;
	}

	/**
	 * Get all roles
	 * @return roles
	 */
	@GetMapping("/role/getAllRoles")
	public BaseResponse<List<Role>> getRoles() {
		return BaseResponse.ok(roleService.findAllRoles());
	}

	/**
	 * Find roles by param
	 * @param pageable Pagination param
	 * @param roleQuery Search param
	 * @return A list of role
	 */
	@GetMapping("/role/list")
	public Page<Role> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = DESC) Pageable pageable,
			RoleQuery roleQuery) {
		return roleService.pageBy(roleQuery, pageable);
	}

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
	 * Add a role
	 *
	 * @param roleParam Param
	 * @return Add result
	 */
	@PostMapping("/role/add")
	public BaseResponse<String> addRole(@RequestBody RoleParam roleParam) {
		roleService.addRole(roleParam);
		return BaseResponse.ok("角色添加成功");
	}

	/**
	 * Update a role
	 *
	 * @param roleParam Param
	 * @return Update result
	 */
	@PostMapping("/role/update")
	public BaseResponse<String> updateRole(@RequestBody RoleParam roleParam) {
		roleService.updateRole(roleParam);
		return BaseResponse.ok("角色更新成功");
	}

	/**
	 * Delete a role
	 *
	 * @param roleId Role id
	 * @return Delete result
	 */
	@PostMapping("/role/delete")
	public BaseResponse<String> deleteRole(@RequestParam Long roleId) {
		roleService.deleteRole(roleId);
		return BaseResponse.ok("角色删除成功");
	}
}
