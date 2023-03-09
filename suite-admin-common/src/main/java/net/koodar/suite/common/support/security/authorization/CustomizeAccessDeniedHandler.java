package net.koodar.suite.common.support.security.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.common.util.JsonUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom access denied handler
 *
 * @author liyc
 */
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		BaseResponse<Object> errorDetail = new BaseResponse<>();
		errorDetail.setCode(HttpStatus.FORBIDDEN.value());
		errorDetail.setMessage("禁止访问");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(errorDetail));
	}

}
