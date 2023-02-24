package net.koodar.suite.admin.module.system.model.params;

import lombok.Data;

/**
 * Permission param.
 *
 * @author liyc
 */
@Data
public class PermissionParam {

	private Long id;

	private Integer type = 1;

	private String parentKey;
	/**
	 * 目录名称
	 */
	private String title;

	/**
	 * 路由名称
	 */
	private String name;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 路由地址
	 */
	private String path;

	/**
	 * 菜单默认路由
	 */
	private String redirect;

	/**
	 * 菜单组件
	 */
	private String component;

	/**
	 * 排序
	 */
	private Integer sort = 0;

	private Boolean keepAlive = true;

}
