package run.bottle.bottleadmin.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.bottle.bottleadmin.model.support.BaseResponse;
import run.bottle.bottleadmin.security.support.AppUserDetails;

/**
 * User controller
 *
 * @author liyc
 * @date 2022-09-10
 */
@RestController
public class UserController {

	@GetMapping("/user/info")
	public BaseResponse<AppUserDetails> index() {
		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return BaseResponse.ok(userDetails);
	}

}
