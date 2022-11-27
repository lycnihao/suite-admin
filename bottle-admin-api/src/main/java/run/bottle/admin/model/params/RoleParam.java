package run.bottle.admin.model.params;

import lombok.Data;

import java.util.List;

/**
 * @author liyc
 * @date 2022-11-27
 */
@Data
public class RoleParam {

	private Long roleId;
	private String name;
	private String code;
	private String description;
	private List<String> permissions;

}
