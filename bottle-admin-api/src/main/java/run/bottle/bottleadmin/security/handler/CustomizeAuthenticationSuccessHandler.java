package run.bottle.bottleadmin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import run.bottle.bottleadmin.cache.AbstractStringCacheStore;
import run.bottle.bottleadmin.model.support.BaseResponse;
import run.bottle.bottleadmin.security.support.AppUserDetails;
import run.bottle.bottleadmin.security.token.AuthToken;
import run.bottle.bottleadmin.security.util.SecurityUtils;
import run.bottle.bottleadmin.util.BottleUtils;
import run.bottle.bottleadmin.util.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Login success handler
 *
 * @author liyc
 * @date 2022-08-26
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
		token.setAccessToken(BottleUtils.randomUUIDWithoutDash());
		token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
		token.setRefreshToken(BottleUtils.randomUUIDWithoutDash());
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
