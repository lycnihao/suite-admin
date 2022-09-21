package run.bottle.bottleadmin.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by service.
 *
 * @author liyc
 * @date 2022-09-01
 */
public class ServiceException extends AbstractBottleException {

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
