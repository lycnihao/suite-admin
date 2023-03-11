package net.koodar.suite.common.support.security.authentication.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.BaseResponse;
import net.koodar.suite.common.support.loginlog.LoginLogService;
import net.koodar.suite.common.support.loginlog.domain.LoginLogResultEnum;
import net.koodar.suite.common.support.security.SecurityConfigurer;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * 登出配置
 *
 * @author liyc
 */
@Component
@RequiredArgsConstructor
public class LogoutConfigurer implements SecurityConfigurer {

	private final LoginLogService loginLogService;

	@Override
	public void configure(HttpSecurity httpSecurity) {
		try {
			httpSecurity
					.logout((authorize) -> authorize
					.logoutSuccessHandler(new CustomizeLogoutSuccessHandler(loginLogService)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Slf4j
	public static class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

		private final LoginLogService loginLogService;

		public CustomizeLogoutSuccessHandler(LoginLogService loginLogService) {
			this.loginLogService = loginLogService;
		}

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

			if (Objects.isNull(authentication)) {
				return;
			}

			// 记录登出日志
			loginLogService.log((AppUserDetails)authentication.getPrincipal(), LoginLogResultEnum.LOGIN_OUT, "");

			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登出成功！", null)));

			log.info("You have been logged out, looking forward to your next visit!");
		}
	}
}
