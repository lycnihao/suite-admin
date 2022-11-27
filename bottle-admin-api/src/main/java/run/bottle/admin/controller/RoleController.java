package run.bottle.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.entity.Role;
import run.bottle.admin.model.params.RoleParam;
import run.bottle.admin.model.params.RoleQuery;
import run.bottle.admin.model.params.UserParam;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.model.vo.RoleVo;
import run.bottle.admin.service.PermissionService;
import run.bottle.admin.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Role controller.
 *
 * @author liyc
 * @date 2022-11-13
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
		roleVo.setRoleCode(role.getCode());
		roleVo.setRoleName(role.getName());
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
	public BaseResponse<String> deleteRole(@RequestBody Long roleId) {
		roleService.findById(roleId);
		return BaseResponse.ok("角色删除成功");
	}
}
