package net.koodar.suite.admin.security.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Access token.
 *
 * @author liyc
 */
@Data
public class AuthToken {

	/**
	 * Access token.
	 */
	@JsonProperty("access_token")
	private String accessToken;

	/**
	 * Expired in. (seconds)
	 */
	@JsonProperty("expired_in")
	private int expiredIn;

	/**
	 * Refresh token.
	 */
	@Deprecated
	@JsonProperty("refresh_token")
	private String refreshToken;

}
