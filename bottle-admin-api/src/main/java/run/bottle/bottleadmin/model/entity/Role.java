package run.bottle.bottleadmin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * 角色表
 *
 * @author liyc
 * @date 2022-09-01
 */
@Data
@Entity
@Table(name = "t_role")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 角色名
	 */
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	/**
	 * 角色描述
	 */
	@Column(name = "description", length = 127)
	private String description;

	/**
	 * 状态: 1.正常 0.禁用
	 */
	@Column(name = "status", length = 2, nullable = false)
	private Integer status;

}
