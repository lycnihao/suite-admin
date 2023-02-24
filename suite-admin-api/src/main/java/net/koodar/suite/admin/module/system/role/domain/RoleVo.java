package net.koodar.suite.admin.module.system.role.domain;

import lombok.Data;

import java.util.List;

/**
 * Role Vo
 *
 * @author liyc
 */
@Data
public class RoleVo {

	private Long roleId;

	private String name;

	private String code;

	private String description;

	private Integer status;

	private List<String> permissions;

}
