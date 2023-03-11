package net.koodar.suite.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录 Controller
 *
 * @author liyc
 */
@Tag(name = "LoginController", description = "账号登录")
@RestController
public class LoginController {

	@PostMapping("/login")
	@Operation(summary = "账户登录")
	public void login(@RequestParam @Parameter(name = "username", description = "用户名", required = true) String username,
					  @RequestParam @Parameter(name = "password", description = "密码", required = true) String password) {
	}

	@PostMapping("/logout")
	@Operation(summary = "账户登出")
	public void logout() {
	}

}
