package net.koodar.suite.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.security.support.AppUserDetails;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import net.koodar.suite.admin.cache.AbstractStringCacheStore;
import net.koodar.suite.admin.model.support.BaseResponse;
import net.koodar.suite.admin.security.util.SecurityUtils;
import net.koodar.suite.admin.util.JsonUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Logout Success Handler
 *
 * @author liyc
 */
@Slf4j
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

	private final AbstractStringCacheStore cacheStore;

	public CustomizeLogoutSuccessHandler(AbstractStringCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		if (Objects.isNull(authentication)) {
			return;
		}

		AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

		// Clear access token
		cacheStore.getAny(SecurityUtils.buildAccessTokenKey(userDetails), String.class)
				.ifPresent(accessToken -> {
					// Delete token
					cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken));
					cacheStore.delete(SecurityUtils.buildAccessTokenKey(userDetails));
				});

		// Clear refresh token
		cacheStore.getAny(SecurityUtils.buildRefreshTokenKey(userDetails), String.class)
				.ifPresent(refreshToken -> {
					cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
					cacheStore.delete(SecurityUtils.buildRefreshTokenKey(userDetails));
				});

		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登出成功！", null)));

		log.info("You have been logged out, looking forward to your next visit!");
	}
}
