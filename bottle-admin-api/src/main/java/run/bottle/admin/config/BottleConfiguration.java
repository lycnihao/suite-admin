package run.bottle.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import run.bottle.admin.cache.AbstractStringCacheStore;
import run.bottle.admin.cache.InMemoryCacheStore;

/**
 * Bottle configuration.
 *
 * @author liyc
 * @date 2022-08-30
 */
@Configuration(proxyBeanMethods = false)
public class BottleConfiguration {

	@Bean
	@ConditionalOnMissingBean
	AbstractStringCacheStore stringCacheStore() {
		return new InMemoryCacheStore();
	}

}
