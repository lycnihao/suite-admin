package run.bottle.bottleadmin.security.support;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import run.bottle.bottleadmin.model.entity.Role;

import java.util.Collection;
import java.util.Set;

/**
 * User Detail
 *
 * @author liyc
 * @date 2022-08-30
 */
@Data
@ToString(callSuper = true)
public class AppUserDetails extends User {

	private Long userId;
	private Set<Role> roles;

	public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public AppUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}


}
