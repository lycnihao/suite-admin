package net.koodar.suite.common.support.security;

import net.koodar.suite.common.support.security.authentication.CustomizeAuthenticationEntryPoint;
import net.koodar.suite.common.support.security.authorization.CustomizeAccessDeniedHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

/**
 * Security 异常配置
 *
 * @author liyc
 */
@Component
public class SecurityExceptionConfigurer implements SecurityConfigurer {
	@Override
	public void configure(HttpSecurity httpSecurity) {
		try {
			httpSecurity
					.exceptionHandling(exceptionHandling -> exceptionHandling
					// 认证失败处理类
					.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
					// 授权失败处理类
					.accessDeniedHandler(new CustomizeAccessDeniedHandler()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
