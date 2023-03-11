package net.koodar.suite.common.support.datatracer;

import net.koodar.suite.common.support.datatracer.domain.DataTracer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据变动记录仓储接口
 *
 * @author liyc
 */
public interface DataTracerRepository extends JpaRepository<DataTracer, Long>, JpaSpecificationExecutor<DataTracer> {
}
