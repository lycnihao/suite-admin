package run.bottle.admin.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.util.JsonUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom access denied handler
 *
 * @author liyc
 * @date 2022-08-29
 */
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		BaseResponse<Object> errorDetail = new BaseResponse<>();
		errorDetail.setStatus(HttpStatus.FORBIDDEN.value());
		errorDetail.setMessage("禁止访问");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(errorDetail));
	}

}
