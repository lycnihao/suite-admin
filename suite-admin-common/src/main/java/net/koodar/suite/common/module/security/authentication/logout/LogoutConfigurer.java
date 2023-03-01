package net.koodar.suite.common.module.security.authentication.logout;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.common.module.security.SecurityConfigurer;
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
public class LogoutConfigurer implements SecurityConfigurer {
	@Override
	public void configure(HttpSecurity httpSecurity) {
		try {
			httpSecurity
					.logout((authorize) -> authorize
					.logoutSuccessHandler(new CustomizeLogoutSuccessHandler()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Slf4j
	public static class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

			if (Objects.isNull(authentication)) {
				return;
			}

			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登出成功！", null)));

			log.info("You have been logged out, looking forward to your next visit!");
		}
	}
}
