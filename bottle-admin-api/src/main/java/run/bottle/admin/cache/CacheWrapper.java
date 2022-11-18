package run.bottle.admin.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Cache wrapper.
 * <p>Part from halo project.</p>
 *
 * @author liyc
 * @date 2022-08-29
 * @see ”run.bottle.bottleadmin.cache.CacheWrapper“
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CacheWrapper<V> implements Serializable {

	/**
	 * Cache data
	 */
	private V data;

	/**
	 * Expired time.
	 */
	private Date expireAt;

	/**
	 * Create time.
	 */
	private Date createAt;

}
