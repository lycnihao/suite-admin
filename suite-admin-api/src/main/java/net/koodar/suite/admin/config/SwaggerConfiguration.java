package net.koodar.suite.admin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration.
 *
 * @author liyc
 */
@Slf4j
@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI suiteOpenApi() {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("SuiteAdmin API")
						.description("SuiteAdmin Api文档")
						.version("0.0.1")
						.contact(new Contact().name("lycnihao").email("38707145@qq.com").url("https://github.com/lycnihao/suite-admin")));
	}

	@Bean
	public GroupedOpenApi allApi() {
		return GroupedOpenApi.builder()
				.group("admin-api")
				.displayName("默认分组")
				.pathsToMatch("/**")
				.build();
	}

}
