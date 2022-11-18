package run.bottle.admin.model.enums;

/**
 * 权限类型
 *
 * @author liyc
 * @date 2022-11-10
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
