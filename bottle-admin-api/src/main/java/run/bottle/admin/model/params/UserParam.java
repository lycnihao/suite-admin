package run.bottle.admin.model.params;

import lombok.Data;

import java.util.Set;

/**
 * User param.
 *
 * @author liyc
 * @date 2022-11-15
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
