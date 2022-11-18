package run.bottle.admin.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * AppUser Details Service
 *
 * @author liyc
 * @date 2022-08-30
 */
public interface AppUserDetailsService extends UserDetailsService {

	UserDetails loadUserById(Long userId) throws UsernameNotFoundException;

}
