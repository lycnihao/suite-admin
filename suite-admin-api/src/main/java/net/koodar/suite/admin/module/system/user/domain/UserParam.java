package net.koodar.suite.admin.module.system.user.domain;

import lombok.Data;

import java.util.Set;

/**
 * User param.
 *
 * @author liyc
 */
@Data
public class UserParam {

	private Long userId;

	private String username;

	private String nickname;

	private String password;

	private String email;

	private String avatar;

	private String description;

	private Set<Long> roleIds;

}
