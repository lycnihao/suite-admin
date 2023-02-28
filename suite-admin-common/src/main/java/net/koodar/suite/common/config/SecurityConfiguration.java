package net.koodar.suite.common.config;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.common.module.security.authentication.CustomizeAuthenticationSuccessHandler;
import net.koodar.suite.common.module.security.authentication.CustomizeLogoutSuccessHandler;
import net.koodar.suite.common.module.security.authentication.AuthenticationTokenFilter;
import net.koodar.suite.common.module.security.authorization.CustomizeAccessDeniedHandler;
import net.koodar.suite.common.module.security.authorization.CustomizeAuthenticationEntryPoint;
import net.koodar.suite.common.module.security.authentication.service.AppUserDetailsService;
import net.koodar.suite.common.module.security.authentication.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Security configuration
 *
 * @author liyc
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtService jwtService;
	private final AppUserDetailsService userDetailsService;
	private final AuthenticationTokenFilter authenticationTokenFilter;
	private final AuthorizationManager<RequestAuthorizationContext> permissionAuthorizationManager;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement((authorize) ->
						authorize.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// springdoc-openapi
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers( "/error").permitAll()
						// 动态权限认证（默认都需要登录）
						.anyRequest().access(permissionAuthorizationManager))
				.formLogin((authorize) -> authorize.permitAll()
						.successHandler(new CustomizeAuthenticationSuccessHandler(jwtService))
						.failureHandler(new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint())))
				.logout((authorize) -> authorize
						.logoutSuccessHandler(new CustomizeLogoutSuccessHandler()))
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
						.accessDeniedHandler(new CustomizeAccessDeniedHandler()))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authenticationTokenFilter, LogoutFilter.class);
		return http.build();
	}

}
