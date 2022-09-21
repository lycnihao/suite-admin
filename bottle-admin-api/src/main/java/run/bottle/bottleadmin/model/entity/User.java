package run.bottle.bottleadmin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


/**
 * 用户表
 *
 * @author liyc
 * @date 2022-09-01
 */
@Data
@Entity
@Table(name = "t_user")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 用户名
	 */
	@Column(name = "username", length = 50, nullable = false)
	private String username;

	/**
	 * 用户昵称
	 */
	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "password", nullable = false)
	private String password;

	/**
	 * 用户邮箱
	 */
	@Column(name = "email", length = 127)
	private String email;

	/**
	 * 用户头像
	 */
	@Column(name = "avatar", length = 1023)
	private String avatar;


	/**
	 * 用户描述
	 */
	@Column(name = "description", length = 127)
	private String description;


	/**
	 * 状态: 1.正常 0.禁用
	 */
	@Column(name = "status")
	private Integer status;

	@Override
	public void prePersist() {
		super.prePersist();

		if (email == null) {
			email = "";
		}

		if (avatar == null) {
			avatar = "";
		}

		if (description == null) {
			description = "";
		}
	}

}
