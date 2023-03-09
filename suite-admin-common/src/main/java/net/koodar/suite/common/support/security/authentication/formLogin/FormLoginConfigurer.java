package net.koodar.suite.common.support.security.authentication.formLogin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.common.support.loginlog.LoginLogService;
import net.koodar.suite.common.support.loginlog.domain.LoginLogResultEnum;
import net.koodar.suite.common.support.security.authentication.jwt.JwtService;
import net.koodar.suite.common.support.security.SecurityConfigurer;
import net.koodar.suite.common.support.security.authentication.CustomizeAuthenticationEntryPoint;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.util.JsonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 表单登录配置
 *
 * @author liyc
 */
@Slf4j
@Configuration
public class FormLoginConfigurer implements SecurityConfigurer {

	private final JwtService jwtService;
	private final LoginLogService loginLogService;

	public FormLoginConfigurer(JwtService jwtService, LoginLogService loginLogService) {
		this.jwtService = jwtService;
		this.loginLogService = loginLogService;
	}

	@Override
	public void configure(HttpSecurity httpSecurity) {
		try {
			httpSecurity
					.formLogin((authorize) -> authorize.permitAll()
					.successHandler(new CustomizeAuthenticationSuccessHandler(loginLogService))
					.failureHandler(new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint())));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		private final LoginLogService loginLogService;

		public CustomizeAuthenticationSuccessHandler(LoginLogService loginLogService) {
			this.loginLogService = loginLogService;
		}

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

			AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

			// 记录登录日志
			loginLogService.log(userDetails, LoginLogResultEnum.LOGIN_SUCCESS, "");

			// 生成Token
			String jwtToken = jwtService.generateToken(userDetails);

			AuthToken token = new AuthToken();
			token.setAccessToken(jwtToken);
			token.setExpiredIn(1000 * 60 * 24);

			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登录成功！", token)));

		}
	}

	@Data
	public static class AuthToken {

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
}
