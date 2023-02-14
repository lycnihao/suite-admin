package net.koodar.suite.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import net.koodar.suite.admin.model.support.BaseResponse;

/**
 * @author liyc
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public BaseResponse<String> index() {
		return BaseResponse.ok("hello world!");
	}

}
