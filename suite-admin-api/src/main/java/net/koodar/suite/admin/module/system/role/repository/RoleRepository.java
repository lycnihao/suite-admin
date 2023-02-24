package net.koodar.suite.admin.module.system.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import net.koodar.suite.admin.module.system.role.domain.Role;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Role Repository.
 *
 * @author liyc
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	/**
	 * Find all by id list.
	 * @param ids id list of role must not be null
	 * @return a list of role
	 */
	List<Role> findAllByIdIn(@NonNull Collection<Long> ids);

	Optional<Role> findByCode(@NonNull String code);

}
