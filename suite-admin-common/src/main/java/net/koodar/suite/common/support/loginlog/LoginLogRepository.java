package net.koodar.suite.common.support.loginlog;

import net.koodar.suite.common.support.loginlog.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 登录日志仓储接口
 *
 * @author liyc
 */
public interface LoginLogRepository extends JpaRepository<LoginLog, Long>, JpaSpecificationExecutor<LoginLog> {
}
