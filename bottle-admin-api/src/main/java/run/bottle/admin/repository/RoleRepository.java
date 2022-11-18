package run.bottle.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import run.bottle.admin.model.entity.Role;

import java.util.Collection;
import java.util.List;

/**
 * Role Repository.
 *
 * @author liyc
 * @date 2022-09-04
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Find all by id list.
	 * @param ids id list of role must not be null
	 * @return a list of role
	 */
	List<Role> findAllByIdIn(@NonNull Collection<Long> ids);

	/**
	 * Find list by status
	 * @param status a status of role must not be null
	 * @return a list of role by status
	 */
	List<Role> findByStatus(@NonNull Integer status);

}
