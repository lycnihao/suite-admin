package run.bottle.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Bottle mvc configuration.
 * @author liyc
 * @date 2022-11-10
 */
@Configuration
public class BottleMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(pageableHandlerMethodArgumentResolver());
	}

	private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
		PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
		pageableResolver.setOneIndexedParameters(true);
		pageableResolver.setFallbackPageable(PageRequest.of(1, 20));
		return pageableResolver;
	}

}
