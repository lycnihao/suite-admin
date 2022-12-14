package run.bottle.admin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Base entity.
 *
 * @author liyc
 * @date 2022-09-01
 */
@Data
@ToString
@MappedSuperclass
@EqualsAndHashCode
public class BaseEntity {

	/**
	 * Create time.
	 */
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * Update time.
	 */
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@PrePersist
	protected void prePersist() {
		Date now = new Date();
		if (createTime == null) {
			createTime = now;
		}

		if (updateTime == null) {
			updateTime = now;
		}
	}

	@PreUpdate
	protected void preUpdate() {
		updateTime = new Date();
	}

	@PreRemove
	protected void preRemove() {
		updateTime = new Date();
	}

}
