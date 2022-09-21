package run.bottle.bottleadmin.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import run.bottle.bottleadmin.model.entity.Permission;
import run.bottle.bottleadmin.model.entity.Role;
import run.bottle.bottleadmin.security.support.AppUserDetails;
import run.bottle.bottleadmin.service.PermissionService;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Permission AuthorizationManager
 *
 * @author liyc
 * @date 2022-09-06
 */
@Slf4j
@Component
public class PermissionAuthorizationManager<T> implements AuthorizationManager<T> {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private final PermissionService permissionService;

	public PermissionAuthorizationManager(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
		// Determines if the current user is authorized by evaluating if the
		boolean granted = isGranted(authentication.get());
		if (!granted) {
			return new AuthorizationDecision(false);
		}

		Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
		Set<String> authority = authorities
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		log.debug("username [{}] hav roles:[{}]", authentication.get().getName(), authority);

		RequestAuthorizationContext requestAuthorizationContext = (RequestAuthorizationContext)object;
		String servletPath = requestAuthorizationContext.getRequest().getRequestURI();
		log.debug("access url:{}", servletPath);

		AppUserDetails userDetails = (AppUserDetails)authentication.get().getPrincipal();
		List<Long> roleIds = userDetails.getRoles().stream().map(Role::getId).collect(Collectors.toList());
		List<Permission> permissions = permissionService.listByRoleIds(roleIds);
		boolean agreeFlag = permissions.stream()
				.anyMatch(permission -> isRouter(permission) && permission.getUrl().equals(servletPath));
		log.debug("check result:{}", agreeFlag);
		return new AuthorizationDecision(agreeFlag);
	}

	private boolean isGranted(Authentication authentication) {
		return authentication != null && isNotAnonymous(authentication) && authentication.isAuthenticated();
	}

	private boolean isNotAnonymous(Authentication authentication) {
		return !this.trustResolver.isAnonymous(authentication);
	}

	private boolean isRouter(Permission permission) {
		return "1".equals(permission.getType());
	}

}
