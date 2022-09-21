package run.bottle.bottleadmin.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import run.bottle.bottleadmin.cache.AbstractStringCacheStore;
import run.bottle.bottleadmin.cache.InMemoryCacheStore;
import run.bottle.bottleadmin.model.entity.*;
import run.bottle.bottleadmin.repository.*;

/**
 * Bottle configuration.
 *
 * @author liyc
 * @date 2022-08-30
 */
@Configuration(proxyBeanMethods = false)
public class BottleConfiguration {

	@Bean
	@ConditionalOnMissingBean
	AbstractStringCacheStore stringCacheStore() {
		return new InMemoryCacheStore();
	}

	/**
	 * 初始化测试数据
	 * 创建两个用户user和admin分别拥有USER和ADMIN角色，ADMIN角色可访问/admin接口
	 *
	 */
	@Bean
	CommandLineRunner initTestUsers(UserRepository userRepository,
									RoleRepository roleRepository,
									UserRoleRepository userRoleRepository,
									PermissionRepository permissionRepository,
									RolePermissionRepository rolePermissionRepository) {
		return args -> {
			User user = new User();
			user.setUsername("admin");
			user.setNickname("admin");
			user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder()
					.encode("123456"));
			user.setEmail("test@example.com");
			user.setStatus(1);
			userRepository.save(user);
			User user1 = new User();
			user1.setUsername("user");
			user1.setNickname("user");
			user1.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder()
					.encode("123456"));
			user1.setEmail("test1@example.com");
			user1.setStatus(1);
			userRepository.save(user1);

			Role role = new Role();
			role.setName("ADMIN");
			role.setStatus(1);
			roleRepository.save(role);
			Role role1 = new Role();
			role1.setName("USER");
			role1.setStatus(1);
			roleRepository.save(role1);

			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(role.getId());
			userRoleRepository.save(userRole);
			UserRole userRole1 = new UserRole();
			userRole1.setUserId(user1.getId());
			userRole1.setRoleId(role1.getId());
			userRoleRepository.save(userRole1);

			Permission permission = new Permission();
			permission.setParentId(0L);
			permission.setName("Admin");
			permission.setOrder(0);
			permission.setType("1");
			permission.setUrl("/admin");
			permissionRepository.save(permission);
			RolePermission rolePermission = new RolePermission();
			rolePermission.setRoleId(role.getId());
			rolePermission.setPermissionId(permission.getId());
			rolePermissionRepository.save(rolePermission);
		};
	}

}
