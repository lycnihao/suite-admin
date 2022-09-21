package run.bottle.bottleadmin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * 资源权限表
 *
 * @author liyc
 * @date 2022-09-01
 */
@Data
@Entity
@Table(name = "t_permission")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 父节点 id
	 */
	@Column(name = "parent_id", nullable = false)
	private Long parentId;

	/**
	 * 资源权限名称
	 */
	@Column(name = "name", length = 127, nullable = false)
	private String name;

	/**
	 * 权限类型: 1.菜单 2.按钮
	 */
	@Column(name = "type", length = 1, nullable = false)
	private String type;

	/**
	 * 图标
	 */
	@Column(name = "icon", length = 32)
	private String icon;

	/**
	 * 访问链接
	 */
	@Column(name = "url", length = 1023, nullable = false)
	private String url;

	/**
	 * 组件地址
	 */
	@Column(name = "component_path", length = 127)
	private String componentPath;

	/**
	 * 排序编号
	 */
	@Column(name = "`order`", nullable = false)
	private Integer order;


}
