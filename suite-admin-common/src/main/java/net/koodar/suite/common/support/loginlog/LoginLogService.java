package net.koodar.suite.common.support.loginlog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.loginlog.domain.LoginLog;
import net.koodar.suite.common.support.loginlog.domain.LoginLogResultEnum;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.util.ServletUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 登录日志业务类
 *
 * @author liyc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogService {

	private final ApplicationEventPublisher eventPublisher;

	public void log(AppUserDetails user, LoginLogResultEnum result, String remark) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		LoginLog loginEntity = LoginLog.builder()
				.userId(user.getUserId())
				.userName(user.getUsername())
				.userAgent(ServletUtils.getHeaderIgnoreCase(request, "user-agent"))
				.loginIp(ServletUtils.getClientIP(request))
				.remark(remark)
				.loginResult(result.getValue())
				.build();
		eventPublisher.publishEvent(loginEntity);
	}

}
