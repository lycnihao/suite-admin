package net.koodar.suite.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.koodar.suite.admin.cache.AbstractStringCacheStore;
import net.koodar.suite.admin.cache.InMemoryCacheStore;

/**
 * App configuration.
 *
 * @author liyc
 */
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

	@Bean
	@ConditionalOnMissingBean
	AbstractStringCacheStore stringCacheStore() {
		return new InMemoryCacheStore();
	}

}
