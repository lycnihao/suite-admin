package net.koodar.suite.common.support.operatelog.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.operatelog.domain.OperateLog;
import net.koodar.suite.common.support.operatelog.repository.OperateLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 操作日志 listener
 *
 * @author liyc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogEventListener {

	private final OperateLogRepository operateLogRepository;

	@EventListener
	@Async
	public void onApplicationEvent(OperateLog operateLog) {
		operateLogRepository.save(operateLog);
	}

}
