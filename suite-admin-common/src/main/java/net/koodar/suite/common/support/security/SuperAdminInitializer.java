package net.koodar.suite.common.support.security;

import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.core.properties.SecurityProperties.Initializer;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

/**
 * 超级管理员初始化处理类
 *
 * @author liyc
 */
@Slf4j
public abstract class SuperAdminInitializer {

	public static final String SUPER_ROLE_NAME = "super-role";

	private final Initializer initializer;

	public SuperAdminInitializer(Initializer initializer) {
		this.initializer = initializer;
	}

	@EventListener
	public void initialize(ApplicationReadyEvent readyEvent) {
		if (!isExistsSuperAdmin()) {
			createAdmin();
		}
	}

	/**
	 * 是否存在超级管理员
	 * @return true: 存在, false: 不存在
	 */
	protected abstract boolean isExistsSuperAdmin();

	/**
	 * 创建超级管理员
	 */
	protected abstract void createAdmin();

	/**
	 * 获取用户名
	 * @return 用户名
	 */
	protected final String getSuperAdminUsername() {
		return this.initializer.getSuperAdminUsername();
	}

	/**
	 * 获取密码，如果没有配置密码则会自动生成密码
	 * @return 密码
	 */
	protected final String getPassword() {
		var password = this.initializer.getSuperAdminPassword();
		if (!StringUtils.hasText(password)) {
			// generate password
			password = RandomStringUtils.randomAlphanumeric(16);
			log.info("=== Generated random password: {} for super administrator: {} ===",
					password, this.initializer.getSuperAdminUsername());
		}
		return password;
	}
}
