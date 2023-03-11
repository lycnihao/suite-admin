package net.koodar.suite.common.properties;

import lombok.Data;

/**
 * Security 属性
 *
 * @author liyc
 */
public class SecurityProperties {

	private final Initializer initializer = new Initializer();

	@Data
	public static class Initializer {

		private boolean disabled;

		private String superAdminUsername = "admin";

		private String superAdminPassword;

	}

}
