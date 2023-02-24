package net.koodar.suite.admin.common.mvc;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.admin.exception.ServiceException;
import net.koodar.suite.admin.common.BaseResponse;
import net.koodar.suite.admin.common.util.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制器异常统一处理
 *
 * @author liyc
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<BaseResponse<Object>> handleServiceException(ServiceException e) {
		BaseResponse<Object> jsonResult = handleBaseException(e);
		jsonResult.setCode(e.getStatus().value());
		jsonResult.setData(e.getErrorData());
		return new ResponseEntity<>(jsonResult, e.getStatus());
	}

	private BaseResponse<Object> handleBaseException(Throwable t) {
		Assert.notNull(t, "Throwable must not be null");

		log.error("Captured an exception", t);

		BaseResponse<Object> jsonResult = new BaseResponse<>();
		jsonResult.setMessage(t.getMessage());
		if (log.isDebugEnabled()) {
			jsonResult.setDevMessage(ExceptionUtils.getStackTrace(t));
		}

		return jsonResult;
	}

}
