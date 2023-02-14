package net.koodar.suite.admin.security.util;

import net.koodar.suite.admin.security.support.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security Context Utils
 *
 * @author liyc
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
