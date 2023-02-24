package net.koodar.suite.admin.module.system.model.params;

import lombok.Data;

import java.util.List;

/**
 * @author liyc
 */
@Data
public class RoleParam {

	private Long roleId;
	private String name;
	private String code;
	private String description;
	private List<String> permissions;

}
