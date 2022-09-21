package run.bottle.bottleadmin.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * common utils
 *
 * @author liyc
 * @date 2022-08-29
 */
public class BottleUtils {

	/**
	 * Gets random uuid without dash.
	 *
	 * @return random uuid without dash
	 */
	@NonNull
	public static String randomUUIDWithoutDash() {
		return StringUtils.remove(UUID.randomUUID().toString(), '-');
	}

}
