package net.koodar.suite.admin.module.system.permission.domain;

/**
 * 权限类型
 *
 * @author liyc
 */
public enum PermissionTypeEnum {

	// 一级菜单
	MENU(1, "一级菜单"),

	// 子菜单
	CHILD_MENU(2, "子菜单"),

	// 按钮权限
	PERMISSIONS(3, "按钮权限");

	private final int value;
	private final String des;

	PermissionTypeEnum(int value, String des) {
		this.value = value;
		this.des = des;
	}

	public int getValue() {
		return value;
	}

	public String getDes() {
		return des;
	}
}
