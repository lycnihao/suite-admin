package net.koodar.suite.admin.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.koodar.suite.admin.model.entity.Permission;

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
