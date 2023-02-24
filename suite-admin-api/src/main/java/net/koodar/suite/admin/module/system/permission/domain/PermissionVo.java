package net.koodar.suite.admin.module.system.permission.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Permission vo
 *
 * @author liyc
 */
@Data
@NoArgsConstructor
public class PermissionVo extends Permission {

	private String parentName;

	private List<PermissionVo> children;

}
