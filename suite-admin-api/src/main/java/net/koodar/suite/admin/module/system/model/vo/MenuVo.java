package net.koodar.suite.admin.module.system.model.vo;

import lombok.Data;

import java.util.Collection;

/**
 * Permission Vo类
 * @author liyc
 */
@Data
public class MenuVo {

	/**
	 * 资源权限名称
	 */
	private String name;

	/**
	 * 资源权限显示名称
	 */
	private String title;

	/**
	 * 路由地址
	 */
	private String path;

	/**
	 * 重定向地址
	 */
	private String redirect;

	/**
	 * 页面组件
	 */
	private String component;

	/**
	 * meta属性，页面标题, 菜单图标等
	 */
	private PermissionMetaVo meta;

	/**
	 * 下级权限
	 */
	private Collection<MenuVo> children;

}
