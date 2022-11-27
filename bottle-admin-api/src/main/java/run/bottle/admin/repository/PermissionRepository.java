package run.bottle.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import run.bottle.admin.model.entity.Permission;

import java.util.Collection;
import java.util.List;

/**
 * Permission repository.
 *
 * @author liyc
 * @date 2022-09-06
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	List<Permission> findByNameIn(Collection<String> names);

}
