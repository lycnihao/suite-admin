package run.bottle.admin.security.util;

import org.springframework.security.core.context.SecurityContextHolder;
import run.bottle.admin.security.support.AppUserDetails;

/**
 * Security Context Utils
 *
 * @author liyc
 * @date 2022-10-26
 */
public class SecurityContextUtils {

	public static AppUserDetails getUserDetails() {
		return  (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static Long getUserId() {
		AppUserDetails userDetails = getUserDetails();
		return  userDetails.getUserId();
	}
}
