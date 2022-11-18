package run.bottle.admin.security.util;

import org.springframework.lang.NonNull;
import run.bottle.admin.security.support.AppUserDetails;

/**
 * Security Utils
 *
 * @author liyc
 * @date 2022-08-29
 */
public class SecurityUtils {

	/**
	 * Access token cache prefix.
	 */
	private static final String TOKEN_ACCESS_CACHE_PREFIX = "bottle.admin.access.token.";

	/**
	 * Refresh token cache prefix.
	 */
	private static final String TOKEN_REFRESH_CACHE_PREFIX = "bottle.admin.refresh.token.";

	private static final String ACCESS_TOKEN_CACHE_PREFIX = "bottle.admin.access_token.";

	private static final String REFRESH_TOKEN_CACHE_PREFIX = "bottle.admin.refresh_token.";

	@NonNull
	public static String buildAccessTokenKey(@NonNull AppUserDetails appUserDetails) {
		return TOKEN_ACCESS_CACHE_PREFIX + appUserDetails.getUserId();
	}

	@NonNull
	public static String buildRefreshTokenKey(@NonNull AppUserDetails appUserDetails) {
		return TOKEN_REFRESH_CACHE_PREFIX + appUserDetails.getUserId();
	}

	@NonNull
	public static String buildTokenAccessKey(@NonNull String accessToken) {
		return ACCESS_TOKEN_CACHE_PREFIX + accessToken;
	}

	@NonNull
	public static String buildTokenRefreshKey(@NonNull String refreshToken) {
		return REFRESH_TOKEN_CACHE_PREFIX + refreshToken;
	}

}
