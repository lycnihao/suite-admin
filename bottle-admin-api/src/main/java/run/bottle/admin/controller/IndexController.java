package run.bottle.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.admin.model.support.BaseResponse;

/**
 * @author liyc
 * @date 2022-08-26
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public BaseResponse<String> index() {
		return BaseResponse.ok("hello world!");
	}

}
