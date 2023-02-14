package net.koodar.suite.admin.config;

import net.koodar.suite.admin.security.authorization.PermissionAuthorizationManager;
import net.koodar.suite.admin.security.filter.AuthenticationTokenFilter;
import net.koodar.suite.admin.security.handler.CustomizeAccessDeniedHandler;
import net.koodar.suite.admin.security.handler.CustomizeAuthenticationEntryPoint;
import net.koodar.suite.admin.security.handler.CustomizeAuthenticationSuccessHandler;
import net.koodar.suite.admin.security.handler.CustomizeLogoutSuccessHandler;
import net.koodar.suite.admin.security.service.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import net.koodar.suite.admin.cache.AbstractStringCacheStore;

/**
 * Security configuration
 *
 * @author liyc
 */
@Configuration
public class SecurityConfiguration {

	private final AppUserDetailsService userDetailsService;
	private final AbstractStringCacheStore cacheStore;
	private final AuthenticationTokenFilter authenticationTokenFilter;
	private final PermissionAuthorizationManager<RequestAuthorizationContext> permissionAuthorizationManager;

	public SecurityConfiguration(AppUserDetailsService userDetailsService, AbstractStringCacheStore cacheStore, PermissionAuthorizationManager<RequestAuthorizationContext> permissionAuthorizationManager) {
		this.userDetailsService = userDetailsService;
		this.cacheStore = cacheStore;
		this.permissionAuthorizationManager = permissionAuthorizationManager;
		this.authenticationTokenFilter = new AuthenticationTokenFilter(cacheStore, userDetailsService);
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				// Spring Security should completely ignore URLs starting with /resources/
				.requestMatchers("/resources/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.userDetailsService(userDetailsService)
				.sessionManagement((authorize) ->
						authorize.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// 动态权限认证（默认都需要登录）
						.anyRequest().access(permissionAuthorizationManager))
				.formLogin((authorize) -> authorize.permitAll()
						.successHandler(new CustomizeAuthenticationSuccessHandler(cacheStore))
						.failureHandler(new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint())))
				.logout((authorize) -> authorize
						.logoutSuccessHandler(new CustomizeLogoutSuccessHandler(cacheStore)))
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
						.accessDeniedHandler(new CustomizeAccessDeniedHandler()))
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authenticationTokenFilter, LogoutFilter.class);
		return http.build();
	}

}
