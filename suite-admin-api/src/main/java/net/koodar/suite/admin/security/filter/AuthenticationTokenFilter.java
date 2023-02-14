package net.koodar.suite.admin.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.security.service.AppUserDetailsService;
import net.koodar.suite.admin.security.service.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

	private static final String AUTHENTICATION_SCHEME = "Bearer ";

	private final AppUserDetailsService appUserDetailsService;
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Get authorization from request header
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;
		final String userEmail;

		if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, AUTHENTICATION_SCHEME)) {
			// Do filter
			filterChain.doFilter(request, response);
			return;
		}

		// Get jwt token
		jwt = authHeader.substring(AUTHENTICATION_SCHEME.length());

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
