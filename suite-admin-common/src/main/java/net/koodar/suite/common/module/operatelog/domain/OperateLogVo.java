package net.koodar.suite.common.module.operatelog.domain;

import lombok.Data;

/**
 * 操作日志 Vo
 *
 * @author liyc
 */
@Data
public class OperateLogVo {

	private String operateUserName;

	private String operateUserType;

	private String module;

	private String content;

	private String url;

	private String ip;

	private String userAgent;

	private String method;

	private String successFlag;

	private String createTime;

}
