package run.bottle.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import run.bottle.admin.cache.AbstractStringCacheStore;
import run.bottle.admin.security.authorization.PermissionAuthorizationManager;
import run.bottle.admin.security.filter.AuthenticationTokenFilter;
import run.bottle.admin.security.handler.CustomizeAuthenticationSuccessHandler;
import run.bottle.admin.security.handler.CustomizeAccessDeniedHandler;
import run.bottle.admin.security.handler.CustomizeAuthenticationEntryPoint;
import run.bottle.admin.security.handler.CustomizeLogoutSuccessHandler;
import run.bottle.admin.security.service.AppUserDetailsService;

/**
 * Security configuration
 *
 * @author liyc
 * @date 2022-08-25
 */
@EnableWebSecurity(debug = false)
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
				.antMatchers("/resources/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/user/info", "/role/info", "/menus", "/role/getAllRoles", "/permission/getAllPermissions").authenticated() // 需要认证
				.anyRequest().access(permissionAuthorizationManager) // 动态权限认证
				.and()
					.userDetailsService(userDetailsService)
					.formLogin()
					.permitAll()
					.successHandler(new CustomizeAuthenticationSuccessHandler(cacheStore))
					.failureHandler(new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint()))
				.and()
					.logout()
					.logoutSuccessHandler(new CustomizeLogoutSuccessHandler(cacheStore))
				.and()
					.exceptionHandling()
					.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
					.accessDeniedHandler(new CustomizeAccessDeniedHandler())
				.and()
					.csrf().disable()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(authenticationTokenFilter, LogoutFilter.class);
		return http.build();
	}

}
