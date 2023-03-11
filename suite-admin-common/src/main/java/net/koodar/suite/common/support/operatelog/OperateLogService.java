package net.koodar.suite.common.support.operatelog;

import cn.hutool.core.date.DateUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import net.koodar.suite.common.exception.ServiceException;
import net.koodar.suite.common.support.operatelog.domain.OperateLog;
import net.koodar.suite.common.support.operatelog.domain.OperateLogQuery;
import net.koodar.suite.common.support.operatelog.OperateLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 操作日志
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class OperateLogService {

	private final OperateLogRepository operateLogRepository;

	public Page<OperateLog> pageBy(OperateLogQuery operateLogQuery, Pageable pageable) {
		return operateLogRepository.findAll(buildSpecByQuery(operateLogQuery), pageable);
	}

	public OperateLog detail(Long operateLogId) {
		Optional<OperateLog> operateLog = operateLogRepository.findById(operateLogId);
		if (operateLog.isEmpty()) {
			throw new ServiceException("非法参数，查询不到id:" + operateLogId);
		}
		return operateLog.get();
	}

	private Specification<OperateLog> buildSpecByQuery(OperateLogQuery operateLogQuery) {
		Assert.notNull(operateLogQuery, "OperateLog query must not be null");
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new LinkedList<>();

			if (StringUtils.hasLength(operateLogQuery.getUsername())) {
				predicates.add(
						criteriaBuilder.like(root.get("operateUserName"), operateLogQuery.getUsername() + "%"));
			}

			if (StringUtils.hasLength(operateLogQuery.getStartDate()) && StringUtils.hasLength(operateLogQuery.getEndDate())) {
				Date startDate = DateUtil.parseDate(operateLogQuery.getStartDate());
				Date endDate = DateUtil.parseDate(operateLogQuery.getEndDate());
				predicates.add(criteriaBuilder.between(root.get("createTime"), startDate, endDate));
			}

			if (operateLogQuery.getSuccessFlag() != null) {
				predicates.add(
						criteriaBuilder.equal(root.get("successFlag"), operateLogQuery.getSuccessFlag()));
			}

			if (predicates.size() > 0) {
				query.where(predicates.toArray(new Predicate[0]));
			}
			return query.getRestriction();
		};
	}
}
