package run.bottle.bottleadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import run.bottle.bottleadmin.model.entity.Permission;

/**
 * Permission repository.
 *
 * @author liyc
 * @date 2022-09-06
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
