package run.bottle.bottleadmin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.bottleadmin.model.support.BaseResponse;

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
