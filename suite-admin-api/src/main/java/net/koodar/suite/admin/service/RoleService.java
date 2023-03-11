package net.koodar.suite.admin.service;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.common.exception.ServiceException;
import net.koodar.suite.admin.manager.RolePermissionManager;
import net.koodar.suite.admin.repository.RoleRepository;
import net.koodar.suite.admin.repository.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import net.koodar.suite.admin.model.entity.Role;
import net.koodar.suite.admin.model.entity.UserRole;
import net.koodar.suite.admin.model.params.RoleParam;
import net.koodar.suite.admin.model.params.RoleQuery;

import jakarta.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Role Service.
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final RolePermissionService rolePermissionService;
	private final RolePermissionManager rolePermissionManager;

	public Role findById(Long id) {
		Optional<Role> optionalRole = roleRepository.findById(id);
		if (optionalRole.isEmpty()) {
			throw new ServiceException("Not found by id");
		}
		return optionalRole.get();
	}

	public List<Role> findListByIds(Collection<Long> ids) {
		return roleRepository.findAllByIdIn(ids);
	}

	public List<Role> findListByUserIds(Collection<Long> userIds) {
		List<UserRole> userRoles = userRoleRepository.findAllByUserIdIn(userIds);
		List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
		return roleRepository.findAllByIdIn(roleIds);
	}

	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	public Page<Role> pageBy(RoleQuery roleQuery, Pageable pageable) {
		// Build specification and find all
		return roleRepository.findAll(buildSpecByQuery(roleQuery), pageable);
	}

	public void addRole(RoleParam roleParam) {
		Optional<Role> optionalRole = roleRepository.findByCode(roleParam.getCode());
		if (optionalRole.isPresent()) {
			throw new ServiceException(String.format("角色编码 [%s] 已存在，请更换后再试吧。", roleParam.getCode()));
		}
		Role role = new Role();
		role.setName(roleParam.getName());
		role.setCode(roleParam.getCode());
		role.setDescription(roleParam.getDescription());
		// 保存角色和权限信息
		rolePermissionManager.updateRolePermissions(role, roleParam.getPermissions());
	}

	public void updateRole(RoleParam roleParam) {
		Optional<Role> optionalRole = roleRepository.findById(roleParam.getRoleId());
		Optional<Role> roleOptional = roleRepository.findByCode(roleParam.getCode());
		if (!optionalRole.isPresent()) {
			throw new ServiceException(String.format("角色id [%s] 不存在。", roleParam.getRoleId()));
		}
		Role role = optionalRole.get();
		if (roleOptional.isPresent() && !roleOptional.get().getId().equals(role.getId())) {
			throw new ServiceException(String.format("角色编码 [%s] 已存在，请更换后再试吧。", roleParam.getCode()));
		}
		role.setName(roleParam.getName());
		role.setCode(roleParam.getCode());
		role.setDescription(roleParam.getDescription());
		// 保存角色和权限信息
		rolePermissionManager.updateRolePermissions(role, roleParam.getPermissions());
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(Long id) {
		roleRepository.deleteById(id);
		rolePermissionService.deleteByRoleId(id);
	}

	private Specification<Role> buildSpecByQuery(RoleQuery roleQuery) {
		Assert.notNull(roleQuery, "Role query must not be null");
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new LinkedList<>();

			if (StringUtils.hasLength(roleQuery.getRoleName())) {
				predicates.add(
						criteriaBuilder.like(root.get("name"), roleQuery.getRoleName() + "%"));
			}
			if (predicates.size() > 0) {
				query.where(predicates.toArray(new Predicate[0]));
			}
			return query.getRestriction();
		};
	}
}
