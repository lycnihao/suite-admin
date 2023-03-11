package net.koodar.suite.admin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import net.koodar.suite.common.support.BaseEntity;

/**
 * 角色表
 *
 * @author liyc
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
	 * 角色编码
	 */
	@Column(name = "code", length = 50, nullable = false)
	private String code;


	/**
	 * 角色描述
	 */
	@Column(name = "description", length = 127)
	private String description;
}
