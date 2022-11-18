package run.bottle.admin.model.entity;

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
	 * 资源权限显示名称
	 */
	@Column(name = "title", length = 127, nullable = false)
	private String title;

	/**
	 * 菜单类型
	 * 1:一级菜单; 2:子菜单; 3:按钮权限
	 */
	@Column(name = "type", length = 127, nullable = false)
	private Integer type = 0;

	/**
	 * 图标
	 */
	@Column(name = "icon", length = 32)
	private String icon;

	/**
	 * 路由地址
	 */
	@Column(name = "path", length = 1023, nullable = false)
	private String path;

	/**
	 * 重定向地址
	 */
	@Column(name = "redirect", length = 1023)
	private String redirect;

	/**
	 * 页面组件
	 */
	@Column(name = "component", length = 127)
	private String component;

	/**
	 * 排序编号
	 */
	@Column(name = "`sort`", nullable = false)
	private Integer sort = 0;

	/**
	 * 缓存该路由
	 */
	@Column(name = "keep_alive", nullable = false)
	private Boolean keepAlive = true;

}
