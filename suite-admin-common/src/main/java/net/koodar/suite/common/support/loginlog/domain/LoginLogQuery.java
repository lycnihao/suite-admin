package net.koodar.suite.common.support.loginlog.domain;

import lombok.Data;

/**
 * 登录日志查询参数
 *
 * @author liyc
 */
@Data
public class LoginLogQuery {

	private String startDate;

	private String endDate;

	private String username;

	private String ip;


}
