package run.bottle.admin.model.params;

import lombok.Data;

/**
 * Permission param.
 *
 * @author liyc
 * @date 2022-12-03
 */
@Data
public class PermissionParam {

	private Long id;

	private String parentKey;

	private String name;

	private String title;

	private Integer type = 1;

	private String icon;

	private String path;

	private String redirect;

	private String component;

	private Integer sort = 0;

	private Boolean keepAlive = true;

}
