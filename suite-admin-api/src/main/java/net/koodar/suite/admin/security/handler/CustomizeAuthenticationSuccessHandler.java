package net.koodar.suite.admin.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.security.support.AppUserDetails;
import net.koodar.suite.admin.security.service.jwt.JwtService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import net.koodar.suite.admin.model.support.BaseResponse;
import net.koodar.suite.admin.security.jwt.AuthToken;
import net.koodar.suite.admin.util.JsonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Login success handler
 *
 * @author liyc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String jwtToken = jwtService.generateToken(userDetails);

		// Generate new token
		AuthToken token = new AuthToken();
		token.setAccessToken(jwtToken);
		token.setExpiredIn(1000 * 60 * 24);

		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登录成功！", token)));

	}
}
