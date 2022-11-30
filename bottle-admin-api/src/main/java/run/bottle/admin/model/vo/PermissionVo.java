package run.bottle.admin.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import run.bottle.admin.model.entity.Permission;

import java.util.List;

/**
 * Permission vo
 *
 * @author liyc
 * @date 2022-11-27
 */
@Data
@NoArgsConstructor
public class PermissionVo extends Permission {

	private String parentName;

	private List<PermissionVo> children;

}
