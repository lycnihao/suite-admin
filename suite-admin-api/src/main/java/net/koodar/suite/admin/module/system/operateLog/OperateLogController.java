package net.koodar.suite.admin.module.system.operateLog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.koodar.suite.common.core.support.BaseResponse;
import net.koodar.suite.common.module.operatelog.domain.OperateLog;
import net.koodar.suite.common.module.operatelog.domain.OperateLogQuery;
import net.koodar.suite.common.module.operatelog.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * 操作日志接口
 *
 * @author liyc
 */
@Tag(name = "OperateLogController", description = "操作日志")
@RestController
public class OperateLogController {

	@Autowired
	private OperateLogService operateLogService;

	@Operation(summary = "查询操作日志列表")
	@GetMapping("/operateLog/list")
	public Page<OperateLog> getUserList(
			@PageableDefault(sort = {"createTime"}, direction = ASC) Pageable pageable,
			OperateLogQuery operateLogQuery) {
		return operateLogService.pageBy(operateLogQuery, pageable);
	}

	@Operation(summary = "查询操作日志详情")
	@GetMapping("/operateLog/detail/{operateLogId}")
	public BaseResponse<OperateLog> detail(@PathVariable Long operateLogId) {
		return BaseResponse.ok(operateLogService.detail(operateLogId));
	}

}
