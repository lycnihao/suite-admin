package net.koodar.suite.admin.config;

import cn.hutool.core.lang.Pair;
import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.module.system.permission.domain.Permission;
import net.koodar.suite.admin.module.system.permission.domain.PermissionTypeEnum;
import net.koodar.suite.admin.module.system.permission.service.PermissionService;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.admin.module.system.role.domain.RolePermission;
import net.koodar.suite.admin.module.system.role.service.RolePermissionService;
import net.koodar.suite.admin.module.system.role.service.RoleService;
import net.koodar.suite.admin.module.system.user.manager.UserRoleManager;
import net.koodar.suite.admin.module.system.user.service.UserService;
import net.koodar.suite.common.core.exception.ServiceException;
import net.koodar.suite.common.core.properties.SecurityProperties;
import net.koodar.suite.common.module.security.SuperAdminInitializer;
import net.koodar.suite.common.module.security.authorization.DynamicSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * security模块相关配置
 *
 * @author liyc
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final PermissionService permissionService;
	private final RoleService roleService;
	private final RolePermissionService rolePermissionService;
	private final UserService userService;
	private final UserRoleManager userRoleManager;

	/**
	 * 加载动态权限
	 */
	@Bean
	public DynamicSecurityService dynamicSecurityService() {
		List<Permission> permissionList = permissionService.getPermissionsByType(PermissionTypeEnum.PERMISSIONS);
		List<Long> permissionIds = permissionList.stream().map(Permission::getId).collect(Collectors.toList());
		List<RolePermission> rolePermissions = rolePermissionService.findByPermissionIdIn(permissionIds);
		List<Role> roleList = roleService.findAllRoles();
		Map<Long, String> roleCodeMap = rolePermissions.stream().collect(Collectors.toMap(RolePermission::getPermissionId, p -> {
			for (Role role : roleList) {
				if (role.getId().equals(p.getRoleId())) {
					return role.getCode();
				}
			}
			throw new ServiceException(String.format("数据异常 角色id[%s]不存在", p.getRoleId()));
		}));
		return new DynamicSecurityService() {
			@Override
			public Map<String, Pair<String, String>> loadDataSource() {
				Map<String, Pair<String, String>> map = new ConcurrentHashMap<>(permissionList.size());
				for (Permission permission : permissionList) {
					map.put(permission.getPath(),
							Pair.of(permission.getName(), roleCodeMap.get(permission.getId())));
				}
				return map;
			}
		};
	}

	/**
	 * 初始化超级管理员账户
	 */
	@Bean
	public SuperAdminInitializer superAdminInitializer() {
		return new SuperAdminInitializer(new SecurityProperties.Initializer()) {
			@Override
			public boolean isExistsSuperAdmin() {
				return userService.loadUserByUsernameThenExists(getSuperAdminUsername());
			}

			@Override
			public void createAdmin() {
				userRoleManager.createAdministrator(getSuperAdminUsername(), getPassword(), SUPER_ROLE_NAME);
			}
		};
	}

}
