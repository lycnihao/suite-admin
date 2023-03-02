package net.koodar.suite.common.config;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.common.module.security.SecurityConfigurer;
import net.koodar.suite.common.module.security.authentication.*;
import net.koodar.suite.common.module.security.authentication.AppUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration
 *
 * @author liyc
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final AppUserDetailsService userDetailsService;
	private final AuthenticationTokenFilter authenticationTokenFilter;

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
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   ObjectProvider<SecurityConfigurer> securityConfigurers) throws Exception {
		http
				// CSRF禁用，因为不使用session
				.csrf().disable()
				// 不开启session会话
				.sessionManagement((authorize) ->
						authorize.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 过滤请求
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// springdoc-openapi
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers( "/error").permitAll()
				)
				.authenticationProvider(authenticationProvider())
				.formLogin(withDefaults())
				.logout(withDefaults())
//				.httpBasic(withDefaults())
				// 自定义 filter
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authenticationTokenFilter, LogoutFilter.class);

		// 与其他 Configurer 集成
		securityConfigurers.orderedStream()
				.forEach(securityConfigurer -> securityConfigurer.configure(http));

		return http.build();
	}

}
