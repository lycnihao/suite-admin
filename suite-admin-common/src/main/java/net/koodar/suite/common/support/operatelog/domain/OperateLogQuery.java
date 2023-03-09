package net.koodar.suite.common.support.operatelog.domain;

import lombok.Data;

/**
 * 操作日志查询参数
 *
 * @author liyc
 */
@Data
public class OperateLogQuery {

	private String username;

	private String startDate;

	private String endDate;

	private Boolean successFlag;


}
