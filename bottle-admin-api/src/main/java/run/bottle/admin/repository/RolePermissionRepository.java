package run.bottle.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import run.bottle.admin.model.entity.RolePermission;

import java.util.Collection;
import java.util.List;

/**
 * RolePermission repository.
 *
 * @author liyc
 * @date 2022-09-07
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

	List<RolePermission> findByPermissionId(Long permissionId);

	List<RolePermission> findByRoleId(Long roleId);

	List<RolePermission> findAllByRoleIdIn(Collection<Long> roleIds);

}
