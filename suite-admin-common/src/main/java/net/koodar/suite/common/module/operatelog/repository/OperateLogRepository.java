package net.koodar.suite.common.module.operatelog.repository;

import net.koodar.suite.common.module.operatelog.domain.OperateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 操作日志 Repository
 *
 * @author liyc
 */
public interface OperateLogRepository extends JpaRepository<OperateLog, Long>, JpaSpecificationExecutor<OperateLog> {
}
