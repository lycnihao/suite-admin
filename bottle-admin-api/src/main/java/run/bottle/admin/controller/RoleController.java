package run.bottle.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.admin.model.entity.Role;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.service.RoleService;

import java.util.List;

/**
 * Role controller.
 *
 * @author liyc
 * @date 2022-11-13
 */
@RestController
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/role/getAllRoles")
	public BaseResponse<List<Role>> getRoles() {
		return BaseResponse.ok(roleService.findAllRoles());
	}
}
