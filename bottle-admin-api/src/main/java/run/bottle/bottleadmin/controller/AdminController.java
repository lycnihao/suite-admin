package run.bottle.bottleadmin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.bottleadmin.model.support.BaseResponse;

/**
 * Admin controller
 *
 * @author liyc
 * @date 2022-09-10
 */
@RestController
public class AdminController {

	@GetMapping("/admin")
	public BaseResponse<String> admin() {
		return BaseResponse.ok("hello admin!");
	}


}
