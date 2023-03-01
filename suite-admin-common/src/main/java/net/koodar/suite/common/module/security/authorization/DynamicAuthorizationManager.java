package net.koodar.suite.common.module.security.authorization;

import cn.hutool.core.lang.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.module.security.authentication.support.AppUserDetails;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.koodar.suite.common.module.security.SuperAdminInitializer.SUPER_ROLE_NAME;

/**
 * Permission AuthorizationManager
 *
 * @author liyc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicAuthorizationManager<T> implements AuthorizationManager<T> {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private final DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
		// Determines if the current user is authorized by evaluating if the
		boolean granted = isGranted(authentication.get());
		if (!granted) {
			return new AuthorizationDecision(false);
		}

		// 超级管理员
		AppUserDetails appUserDetails = (AppUserDetails) authentication.get().getPrincipal();
		if (appUserDetails.getAdministratorFlag()) {
			return new AuthorizationDecision(true);
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

		if (dynamicSecurityMetadataSource.checkPermissionUrl(servletPath)) {
			Collection<Pair<String, String>> configAttributes = dynamicSecurityMetadataSource.getAttributes(requestAuthorizationContext);
			for (Pair<String, String> configAttribute : configAttributes) {
				//将访问所需资源或用户拥有资源进行比对
				String needAuthority = configAttribute.getValue();
				if (!authority.contains(needAuthority)) {
					return new AuthorizationDecision(false);
				}
			}
		}
		return new AuthorizationDecision(true);
	}

	private boolean isGranted(Authentication authentication) {
		return authentication != null && isNotAnonymous(authentication) && authentication.isAuthenticated();
	}

	private boolean isNotAnonymous(Authentication authentication) {
		return !this.trustResolver.isAnonymous(authentication);
	}

}
