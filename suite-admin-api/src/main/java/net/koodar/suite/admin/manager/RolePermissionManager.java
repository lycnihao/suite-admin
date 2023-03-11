package net.koodar.suite.admin.manager;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.model.entity.Role;
import net.koodar.suite.admin.repository.RoleRepository;
import net.koodar.suite.admin.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限 manager
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class RolePermissionManager {

	private final RoleRepository roleRepository;
	private final RolePermissionService rolePermissionService;

	/**
	 * 更新角色具有的权限
	 *
	 * @param role 角色
	 * @param permissions 权限
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRolePermissions(Role role, List<String> permissions) {
		// 保存角色
		roleRepository.save(role);
		// 更新角色权限信息
		rolePermissionService.bindRoleWithUser(role.getId(), permissions);
	}

}
