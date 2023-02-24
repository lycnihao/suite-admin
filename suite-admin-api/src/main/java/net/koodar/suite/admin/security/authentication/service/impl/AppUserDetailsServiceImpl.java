package net.koodar.suite.admin.security.authentication.service.impl;

import net.koodar.suite.admin.module.system.role.repository.RoleRepository;
import net.koodar.suite.admin.module.system.user.repository.UserRepository;
import net.koodar.suite.admin.security.authentication.service.AppUserDetailsService;
import net.koodar.suite.admin.security.authentication.AppUserDetails;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import net.koodar.suite.admin.module.system.role.domain.Role;
import net.koodar.suite.admin.module.system.user.domain.User;
import net.koodar.suite.admin.module.system.role.domain.UserRole;
import net.koodar.suite.admin.module.system.role.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AppUser Details ServiceImpl
 *
 * @author liyc
 */
@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final RoleRepository roleRepository;

	public AppUserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.roleRepository = roleRepository;
	}

	/**
	 * load user details by username
	 * @param username username must not null
	 * @return userDetails of AppUserDetails
	 * @throws UsernameNotFoundException not found exception
	 */
	@Override
	public AppUserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getDeletedFlag()) {
				throw new UsernameNotFoundException(String.format("Username [%s] unavailable", username));
			}
			List<Role> roles = getRoleByUserId(user.getId());
			return buildAppUserDetails(user, roles);
		}
		throw new UsernameNotFoundException(String.format("Username [%s] not found in db", username));
	}

	/**
	 * userId user details by username
	 * @param userId userId must not null
	 * @return userDetails of AppUserDetails
	 * @throws UsernameNotFoundException not found exception
	 */
	@Override
	public UserDetails loadUserById(@NonNull Long userId) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Role> roles = getRoleByUserId(user.getId());
			return buildAppUserDetails(user, roles);
		}
		throw new UsernameNotFoundException(String.format("UserId [%s] not found in db", userId));
	}

	/**
	 * Get role by userId
	 * @param userId userId must not be null
	 * @return a list of Role
	 */
	private List<Role> getRoleByUserId(@NonNull Long userId) {
		List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
		if (!userRoles.isEmpty()) {
			List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
			return roleRepository.findAllByIdIn(roleIds);
		}
		return new ArrayList<>();
	}

	/**
	 * build appUserDetails with user and role
	 * @param user user must not be null
	 * @param roles roles must not be null
	 * @return an userDetails
	 */
	private AppUserDetails buildAppUserDetails(@NonNull User user, @NonNull List<Role> roles) {
		List<SimpleGrantedAuthority> authorityList = roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getCode()))
				.collect(Collectors.toList());
		AppUserDetails appUserDetails = new AppUserDetails(user.getUsername(), user.getPassword(), authorityList);
		appUserDetails.setUserId(user.getId());
		appUserDetails.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toSet()));
		return appUserDetails;
	}
}
