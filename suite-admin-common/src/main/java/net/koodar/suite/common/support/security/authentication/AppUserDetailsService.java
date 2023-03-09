package net.koodar.suite.common.support.security.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * AppUser Details Service
 *
 * @author liyc
 */
public interface AppUserDetailsService extends UserDetailsService {

	UserDetails loadUserById(Long userId) throws UsernameNotFoundException;

	Boolean loadUserByUsernameThenExists(String username);

}
