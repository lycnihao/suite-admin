package run.bottle.bottleadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import run.bottle.bottleadmin.model.entity.RolePermission;

import java.util.List;

/**
 * RolePermission repository.
 *
 * @author liyc
 * @date 2022-09-07
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

	List<RolePermission> findAllByRoleIdIn(List<Long> roleIds);

}
