package net.koodar.suite.admin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import net.koodar.suite.common.support.BaseEntity;

/**
 * @author liyc
 */
@Data
@Entity
@Table(name = "t_role_permission")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RolePermission extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 角色 id
	 */
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	/**
	 * 资源权限 id
	 */
	@Column(name = "permission_id", nullable = false)
	private Long permissionId;


}
