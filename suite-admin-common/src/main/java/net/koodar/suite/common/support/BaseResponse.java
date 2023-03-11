package net.koodar.suite.common.support;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Global response entity.
 *
 * @author liyc
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

	/**
	 * Response code.
	 */
	private Integer code;

	/**
	 * Response message.
	 */
	private String message;

	/**
	 * Response development message
	 */
	private String devMessage;

	/**
	 * Response data
	 */
	private T data;

	public BaseResponse(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * Creates an ok result with message and data. (Default status is 200)
	 *
	 * @param data result data
	 * @param message result message
	 * @return ok result with message and data
	 */
	@NonNull
	public static <T> BaseResponse<T> ok(@Nullable String message, @Nullable T data) {
		return new BaseResponse<>(HttpStatus.OK.value(), message, data);
	}

	/**
	 * Creates an ok result with message only. (Default status is 200)
	 *
	 * @param message result message
	 * @return ok result with message only
	 */
	@NonNull
	public static <T> BaseResponse<T> ok(@Nullable String message) {
		return ok(message, null);
	}

	/**
	 * Creates an ok result with data only. (Default message is OK, status is 200)
	 *
	 * @param data data to response
	 * @param <T> data type
	 * @return base response with data
	 */
	public static <T> BaseResponse<T> ok(@Nullable T data) {
		return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
	}

}