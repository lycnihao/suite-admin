package net.koodar.suite.common.support.datatracer.domain;

import jakarta.persistence.*;
import lombok.*;
import net.koodar.suite.common.support.BaseEntity;

/**
 * 数据记录
 *
 * @author liyc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_data_tracer")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataTracer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 数据id
	 */
	@Column(name = "data_id")
	private Long dataId;
	/**
	 * 业务类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * diff 差异：旧的数据
	 */
	@Column(name = "diff_old")
	private String diffOld;

	/**
	 * 差异：新的数据
	 */
	@Column(name = "diff_new")
	private String diffNew;

	/**
	 * 扩展字段
	 */
	@Column(name = "extra_data")
	private String extraData;

	/**
	 * 用户
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String username;

	/**
	 * 请求ip
	 */
	@Column(name = "ip")
	private String ip;

	/**
	 * 请求头
	 */
	@Column(name = "user_agent")
	private String userAgent;

}
