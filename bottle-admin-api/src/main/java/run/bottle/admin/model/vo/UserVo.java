package run.bottle.admin.model.vo;

import lombok.Data;

import java.util.Set;

/**
 * UserInfo Vo类
 * @author liyc
 * @date 2022-10-26
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

	private Set<PairVo<String>> permissions;
	public UserVo() {
	}
}
