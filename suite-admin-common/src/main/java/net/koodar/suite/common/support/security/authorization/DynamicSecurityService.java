package net.koodar.suite.common.support.security.authorization;

import cn.hutool.core.lang.Pair;

import java.util.Map;

/**
 * 动态权限相关业务接口
 *
 * @author liyc
 */
public interface DynamicSecurityService {

	/**
	 * 加载资源权限
	 * @return 资源权限
	 */
	Map<String, Pair<String, String>> loadDataSource();

}
