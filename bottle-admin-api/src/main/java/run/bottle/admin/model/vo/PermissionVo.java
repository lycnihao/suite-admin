package run.bottle.admin.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Permission vo
 *
 * @author liyc
 * @date 2022-11-27
 */
@Data
public class PermissionVo {

	private String name;

	private String title;

	private List<PermissionVo> children;

}
