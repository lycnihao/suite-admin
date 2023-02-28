package net.koodar.suite.common.module.security.authorization;

import cn.hutool.core.lang.Pair;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 动态权限数据源，用于获取动态权限规则
 *
 * @author liyc
 */
@Component
@RequiredArgsConstructor
public class DynamicSecurityMetadataSource {

	/**
	 * 资源权限集合
	 * key: 路径, value: 权限编号, 角色编号
	 */
	private static Map<String, Pair<String, String>> configAttributeMap = null;

	private final DynamicSecurityService dynamicSecurityService;
	@PostConstruct
	public void loadDataSource() {
		configAttributeMap = dynamicSecurityService.loadDataSource();
	}

	public void clearDataSource() {
		configAttributeMap.clear();
		configAttributeMap = null;
	}

	public boolean checkPermissionUrl (String path) {
		if (!StringUtils.hasLength(path)) {
			return false;
		}
		return configAttributeMap.containsKey(path);
	}

	public Collection<Pair<String, String>> getAttributes(RequestAuthorizationContext req) throws IllegalArgumentException {
		if (configAttributeMap == null) {
			this.loadDataSource();
		}
		List<Pair<String, String>> configAttributes = new ArrayList<>();
		//获取当前访问的路径
		String path = req.getRequest().getRequestURI();
		PathMatcher pathMatcher = new AntPathMatcher();
		//获取访问该路径所需资源
		for (String pattern : configAttributeMap.keySet()) {
			if (pathMatcher.match(pattern, path)) {
				configAttributes.add(configAttributeMap.get(pattern));
			}
		}
		// 未设置操作请求权限，返回空集合
		return configAttributes;
	}
}
