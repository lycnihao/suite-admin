package run.bottle.admin.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Role Vo
 *
 * @author liyc
 * @date 2022-11-27
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
