package run.bottle.bottleadmin.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import run.bottle.bottleadmin.cache.AbstractStringCacheStore;
import run.bottle.bottleadmin.security.service.AppUserDetailsService;
import run.bottle.bottleadmin.security.util.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Authentication Token Filter.
 *
 * @author liyc
 * @date 2022-08-30
 */
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private static final String AUTHENTICATION_SCHEME_BEARER = "Bearer";

	private final AbstractStringCacheStore cacheStore;
	private final AppUserDetailsService appUserDetailsService;

	public AuthenticationTokenFilter(AbstractStringCacheStore cacheStore, AppUserDetailsService appUserDetailsService) {
		this.cacheStore = cacheStore;
		this.appUserDetailsService = appUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Get token from request header
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.hasText(accessToken)) {
			// Do filter
			filterChain.doFilter(request, response);
			return;
		}

		if (!StringUtils.startsWithIgnoreCase(accessToken, AUTHENTICATION_SCHEME_BEARER)) {
			throw new BadCredentialsException("Token 必须以bearer开头");
		}
		if (accessToken.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
			throw new BadCredentialsException("Token 不能为空");
		}

		// Get token body
		accessToken = accessToken.substring(AUTHENTICATION_SCHEME_BEARER.length() + 1);

		Optional<Long> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(accessToken), Long.class);
		if (!optionalUserId.isPresent()) {
			log.debug("Token 已过期或不存在 [{}]", accessToken);
			filterChain.doFilter(request, response);
			return;
		}

		UserDetails userDetails = appUserDetailsService.loadUserById(optionalUserId.get());
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Do filter
		filterChain.doFilter(request, response);
	}

}
