package net.koodar.suite.common.support.security.authentication.support;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

/**
 * User Detail
 *
 * @author liyc
 */
@Data
@ToString(callSuper = true)
public class AppUserDetails extends User {

	private Long userId;
	private Set<Long> roleIds;
	private Boolean administratorFlag;

	public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public AppUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}


}
