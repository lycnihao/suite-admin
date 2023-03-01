package net.koodar.suite.common.module.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 自定义配置
 *
 * @author liyc
 */
public interface SecurityConfigurer {

	void configure(HttpSecurity httpSecurity);

}
