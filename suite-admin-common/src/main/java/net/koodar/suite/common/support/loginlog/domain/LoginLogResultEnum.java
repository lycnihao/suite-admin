package net.koodar.suite.common.support.loginlog.domain;

/**
 * 登录类型
 *
 * @author liyc
 */
public enum LoginLogResultEnum {

	LOGIN_SUCCESS(0, "登录成功"),
	LOGIN_FAIL(1, "登录失败"),
	LOGIN_OUT(2, "退出登录");

	private final Integer type;
	private final String desc;

	LoginLogResultEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getValue() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
