package net.koodar.suite.common.support.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.security.authentication.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication Token Filter.
 *
 * @author liyc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();
	private final AuthenticationEntryPointFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint());

	private static final String AUTHENTICATION_SCHEME = "Bearer ";

	private final AppUserDetailsService appUserDetailsService;
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Get authorization from request header
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;
		final String userEmail;

		var isLogin = "/login".equals(request.getServletPath());
		if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, AUTHENTICATION_SCHEME) || isLogin) {
			// Do filter
			filterChain.doFilter(request, response);
			return;
		}

		// Get jwt token
		jwt = authHeader.substring(AUTHENTICATION_SCHEME.length());

		if (jwtService.isTokenExpired(jwt)) {
			this.securityContextHolderStrategy.clearContext();
			this.failureHandler.onAuthenticationFailure(request, response, new CredentialsExpiredException("token过期请重新登录"));
			return;
		}

		userEmail = jwtService.extractUsername(jwt);

		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = appUserDetailsService.loadUserByUsername(userEmail);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities()
				);
				authentication.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		// Do filter
		filterChain.doFilter(request, response);
	}

}
