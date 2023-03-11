package net.koodar.suite.common.support.loginlog.domain;

import jakarta.persistence.*;
import lombok.*;
import net.koodar.suite.common.support.BaseEntity;

/**
 * 登录日志
 *
 * @author liyc
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_login_log")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 登录ip
	 */
	@Column(name = "login_ip")
	private String loginIp;

	/**
	 * user-agent
	 */
	@Column(name = "user_agent")
	private String userAgent;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 登录类型
	 */
	@Column(name = "login_result")
	private Integer loginResult;

}
