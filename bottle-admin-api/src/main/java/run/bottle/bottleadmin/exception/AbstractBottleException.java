package run.bottle.bottleadmin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base exception of the project.
 *
 * @author liyc
 * @date 2022-09-01
 */
public abstract class AbstractBottleException extends RuntimeException {

	/**
	 * Error errorData.
	 */
	private Object errorData;

	public AbstractBottleException(String message) {
		super(message);
	}

	public AbstractBottleException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Http status code
	 *
	 * @return {@link HttpStatus}
	 */
	@NonNull
	public abstract HttpStatus getStatus();

	@Nullable
	public Object getErrorData() {
		return errorData;
	}

	/**
	 * Sets error errorData.
	 *
	 * @param errorData error data
	 * @return current exception.
	 */
	@NonNull
	public AbstractBottleException setErrorData(@Nullable Object errorData) {
		this.errorData = errorData;
		return this;
	}
}
