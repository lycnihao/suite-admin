package run.bottle.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import run.bottle.admin.model.support.BaseResponse;
import run.bottle.admin.util.ExceptionUtils;
import run.bottle.admin.util.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Login failure handler
 *
 * @author liyc
 * @date 2022-08-26
 */
@Slf4j
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		BaseResponse<Object> errorDetail = handleBaseException(authException);
		errorDetail.setData(Collections.singletonMap("uri", request.getRequestURI()));
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(errorDetail));
	}

	private BaseResponse<Object> handleBaseException(Throwable t) {
		Assert.notNull(t, "Throwable must not be null");

		BaseResponse<Object> errorDetail = new BaseResponse<>();
		errorDetail.setStatus(HttpStatus.UNAUTHORIZED.value());

		if (log.isDebugEnabled()) {
			errorDetail.setDevMessage(ExceptionUtils.getStackTrace(t));
		}

		if (t instanceof AccountExpiredException){
			errorDetail.setMessage("账户过期");
		} else if (t instanceof DisabledException){
			errorDetail.setMessage("账号被禁用");
		} else if (t instanceof LockedException){
			errorDetail.setMessage("账户被锁定");
		} else if (t instanceof AuthenticationCredentialsNotFoundException){
			errorDetail.setMessage("用户身份凭证未找到");
		} else if (t instanceof AuthenticationServiceException){
			errorDetail.setMessage("用户身份认证服务异常");
		} else if (t instanceof BadCredentialsException){
			errorDetail.setMessage(t.getMessage());
		} else {
			errorDetail.setMessage("访问未授权");
		}

		return errorDetail;
	}
}
