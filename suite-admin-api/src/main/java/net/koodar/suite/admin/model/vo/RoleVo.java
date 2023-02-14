package net.koodar.suite.admin.model.vo;

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

	private String roleName;

	private String roleCode;

	private String description;

	private Integer status;

	private List<String> permissions;

}