package net.koodar.suite.admin.module.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.koodar.suite.admin.module.system.model.entity.Permission;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Permission repository.
 *
 * @author liyc
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Optional<Permission> findByName(String name);

	List<Permission> findByNameIn(Collection<String> names);

	List<Permission> findByType(Integer type);

}
