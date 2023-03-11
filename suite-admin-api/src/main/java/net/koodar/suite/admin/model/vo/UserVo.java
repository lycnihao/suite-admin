package net.koodar.suite.admin.model.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * UserInfo Vo类
 * @author liyc
 */
@Data
public class UserVo {

	private Long userId;

	private String username;

	private String nickname;

	private String email;

	private String avatar;

	private Set<String> roles;

	private Set<Long> roleIds;

	private Set<Map<String, String>> permissions;
	public UserVo() {
	}
}
