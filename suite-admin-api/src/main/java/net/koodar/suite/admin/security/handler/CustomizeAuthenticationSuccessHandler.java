package net.koodar.suite.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.security.support.AppUserDetails;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import net.koodar.suite.admin.cache.AbstractStringCacheStore;
import net.koodar.suite.admin.model.support.BaseResponse;
import net.koodar.suite.admin.security.token.AuthToken;
import net.koodar.suite.admin.security.util.SecurityUtils;
import net.koodar.suite.admin.util.SuiteUtils;
import net.koodar.suite.admin.util.JsonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Login success handler
 *
 * @author liyc
 */
@Slf4j
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AbstractStringCacheStore cacheStore;

	/**
	 * Expired seconds.
	 */
	private static final int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

	private static final int REFRESH_TOKEN_EXPIRED_DAYS = 30;

	public CustomizeAuthenticationSuccessHandler(AbstractStringCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Generate new token
		AuthToken token = new AuthToken();
		token.setAccessToken(SuiteUtils.randomUUIDWithoutDash());
		token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
		token.setRefreshToken(SuiteUtils.randomUUIDWithoutDash());
		// Cache those tokens, just for clearing
		cacheStore.putAny(SecurityUtils.buildAccessTokenKey(userDetails), token.getAccessToken(),
				ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
		cacheStore.putAny(SecurityUtils.buildRefreshTokenKey(userDetails), token.getRefreshToken(),
				REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

		// Cache those tokens with user id
		cacheStore.putAny(SecurityUtils.buildTokenAccessKey(token.getAccessToken()), userDetails.getUserId(),
				ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
		cacheStore.putAny(SecurityUtils.buildTokenRefreshKey(token.getRefreshToken()), userDetails.getUserId(),
				REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登录成功！", token)));

	}
}
