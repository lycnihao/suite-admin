package net.koodar.suite.admin.controller.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.koodar.suite.common.support.loginlog.LoginLogService;
import net.koodar.suite.common.support.loginlog.domain.LoginLog;
import net.koodar.suite.common.support.loginlog.domain.LoginLogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * 登录日志
 *
 * @author liyc
 */
@Tag(name = "LoginLogController", description = "登录日志")
@RestController
@RequiredArgsConstructor
public class LoginLogController {

	private final LoginLogService loginLogService;

	@Operation(summary = "查询登录日志列表")
	@GetMapping("/loginLog/list")
	public Page<LoginLog> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = DESC) Pageable pageable,
			LoginLogQuery loginLogQuery) {
		return loginLogService.pageBy(loginLogQuery, pageable);
	}

}
