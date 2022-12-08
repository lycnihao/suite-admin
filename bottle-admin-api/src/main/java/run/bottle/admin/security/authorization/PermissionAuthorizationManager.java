package run.bottle.admin.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import run.bottle.admin.model.entity.Permission;
import run.bottle.admin.model.enums.PermissionTypeEnum;
import run.bottle.admin.security.support.AppUserDetails;
import run.bottle.admin.service.PermissionService;

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
		List<Permission> permissionAllLis = permissionService.getPermissionsByType(PermissionTypeEnum.PERMISSIONS);
		if (permissionAllLis.stream().anyMatch(permission -> StringUtils.hasLength(permission.getPath()) &&
						permission.getPath().equals(servletPath))) {
			List<Permission> permissions = permissionService.listByRoleIds(userDetails.getRoleIds());
			boolean agreeFlag = permissions.stream()
					.anyMatch(permission -> isUrlPermission(permission) && permission.getPath().equals(servletPath));
			log.debug("check result:{}", agreeFlag);
			return new AuthorizationDecision(agreeFlag);
		}
		return new AuthorizationDecision(true);
	}

	private boolean isGranted(Authentication authentication) {
		return authentication != null && isNotAnonymous(authentication) && authentication.isAuthenticated();
	}

	private boolean isNotAnonymous(Authentication authentication) {
		return !this.trustResolver.isAnonymous(authentication);
	}

	private boolean isUrlPermission(Permission permission) {
		return permission.getType() == PermissionTypeEnum.PERMISSIONS.getValue();
	}

}
