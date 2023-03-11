package net.koodar.suite.admin.model.vo;

import lombok.Data;

import java.util.Set;

/**
 * Meta Vo
 *
 * @author liyc
 */
@Data
public class PermissionMetaVo {

	/**
	 * 菜单名称
	 */
	private String title;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 排序编号
	 */
	private Integer sort;

	/**
	 * 缓存该路由
	 */
	private Boolean keepAlive;

	/**
	 * 下级权限
	 */
	private Set<String> permissions;

}
