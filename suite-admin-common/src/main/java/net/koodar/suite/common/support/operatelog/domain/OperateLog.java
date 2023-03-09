package net.koodar.suite.common.support.operatelog.domain;

import jakarta.persistence.*;
import lombok.*;
import net.koodar.suite.common.core.support.BaseEntity;

/**
 * 操作日志
 *
 * @author liyc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_operate_log")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OperateLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 操作人id
	 */
	@Column(name = "operate_user_id", nullable = false)
	private Long operateUserId;

	/**
	 * 操作人名称
	 */
	@Column(name = "operate_user_name", length = 63, nullable = false)
	private String operateUserName;
	/**
	 * 操作模块
	 */
	@Column(name = "module", length = 31, nullable = false)
	private String module;

	/**
	 * 操作内容
	 */
	@Column(name = "content", length = 1023)
	private String content;

	/**
	 * 请求路径
	 */
	@Column(name = "url", length = 1023, nullable = false)
	private String url;

	/**
	 * 请求方法
	 */
	@Column(name = "method", length = 127, nullable = false)
	private String method;

	/**
	 * 请求参数
	 */
	@Column(name = "param", length = 1023, nullable = false)
	private String param;

	/**
	 * 客户ip
	 */
	@Column(name = "ip", length = 127, nullable = false)
	private String ip;

	/**
	 * user-agent
	 */
	@Column(name = "user_agent", length = 127, nullable = false)
	private String userAgent;

	/**
	 * 请求结果 0失败 1成功
	 */
	@Column(name = "success_flag", nullable = false)
	private Boolean successFlag;

	/**
	 * 失败原因
	 */
	@Column(name = "fail_reason", length = 1023)
	private String failReason;
}
