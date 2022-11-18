package run.bottle.admin.model.vo;

import lombok.Data;

import java.util.Set;

/**
 * Meta Vo
 *
 * @author liyc
 * @date 2022-11-01
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
