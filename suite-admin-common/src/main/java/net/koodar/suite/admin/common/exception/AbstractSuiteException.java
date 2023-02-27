package net.koodar.suite.admin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base exception of the project.
 *
 * @author liyc
 */
public abstract class AbstractSuiteException extends RuntimeException {

	/**
	 * Error errorData.
	 */
	private Object errorData;

	public AbstractSuiteException(String message) {
		super(message);
	}

	public AbstractSuiteException(String message, Throwable cause) {
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
	public AbstractSuiteException setErrorData(@Nullable Object errorData) {
		this.errorData = errorData;
		return this;
	}
}
