package net.koodar.suite.common.support.loginlog.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.loginlog.LoginLogRepository;
import net.koodar.suite.common.support.loginlog.domain.LoginLog;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 登录日志 listener
 *
 * @author liyc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginLogEventListener {

	private final LoginLogRepository loginLogRepository;

	@EventListener
	@Async
	public void onApplicationEvent(LoginLog loginLog) {
		try {
			loginLogRepository.save(loginLog);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
