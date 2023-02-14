package net.koodar.suite.admin.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * User list vo.
 *
 * @author liyc
 */
@Data
public class UserListVo {

	private Long userId;

	private String username;

	private String nickname;

	private String email;

	private String avatar;

	private Date createDate;

	private Set<String> roles;

}
