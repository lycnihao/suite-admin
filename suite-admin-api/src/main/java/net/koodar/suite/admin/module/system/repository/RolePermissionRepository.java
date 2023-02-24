package net.koodar.suite.admin.module.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.koodar.suite.admin.module.system.model.entity.RolePermission;

import java.util.Collection;
import java.util.List;

/**
 * RolePermission repository.
 *
 * @author liyc
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

	List<RolePermission> findByPermissionId(Long permissionId);

	List<RolePermission> findByRoleId(Long roleId);

	List<RolePermission> findAllByRoleIdIn(Collection<Long> roleIds);

}
