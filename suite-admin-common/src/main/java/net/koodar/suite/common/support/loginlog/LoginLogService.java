package net.koodar.suite.common.support.loginlog;

import cn.hutool.core.date.DateUtil;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.support.loginlog.domain.LoginLog;
import net.koodar.suite.common.support.loginlog.domain.LoginLogQuery;
import net.koodar.suite.common.support.loginlog.domain.LoginLogResultEnum;
import net.koodar.suite.common.support.security.authentication.support.AppUserDetails;
import net.koodar.suite.common.util.ServletUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
	private final LoginLogRepository loginLogRepository;

	/**
	 * 查询登录日志
	 * @param loginLogQuery 请求参数
	 * @param pageable 分页参数
	 * @return 登录日志列表
	 */
	public Page<LoginLog> pageBy(LoginLogQuery loginLogQuery, Pageable pageable) {
		return loginLogRepository.findAll(buildSpecByQuery(loginLogQuery), pageable);
	}

	/**
	 * 保存登录日志
	 * @param user 用户Details
	 * @param result 登录类型
	 * @param remark 备注
	 */
	public void log(AppUserDetails user, LoginLogResultEnum result, String remark) {
		LoginLog loginEntity = LoginLog.builder()
				.userId(user.getUserId())
				.userName(user.getUsername())
				.userAgent(user.getUserAgent())
				.loginIp(user.getIp())
				.remark(remark)
				.loginResult(result.getValue())
				.build();
		eventPublisher.publishEvent(loginEntity);
	}

	private Specification<LoginLog> buildSpecByQuery(LoginLogQuery loginLogQuery) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new LinkedList<>();

			if (StringUtils.hasLength(loginLogQuery.getUsername())) {
				predicates.add(
						criteriaBuilder.like(root.get("userName"), loginLogQuery.getUsername() + "%"));
			}
			if (StringUtils.hasLength(loginLogQuery.getIp())) {
				predicates.add(
						criteriaBuilder.equal(root.get("loginIp"), loginLogQuery.getIp()));
			}
			if (StringUtils.hasLength(loginLogQuery.getStartDate()) && StringUtils.hasLength(loginLogQuery.getEndDate())) {
				Date startDate = DateUtil.parseDate(loginLogQuery.getStartDate());
				Date endDate = DateUtil.parseDate(loginLogQuery.getEndDate());
				predicates.add(criteriaBuilder.between(root.get("createTime"), startDate, endDate));
			}
			if (predicates.size() > 0) {
				query.where(predicates.toArray(new Predicate[0]));
			}
			return query.getRestriction();
		};
	}

}
