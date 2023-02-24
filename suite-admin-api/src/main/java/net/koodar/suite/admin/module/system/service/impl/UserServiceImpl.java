package net.koodar.suite.admin.module.system.service.impl;

import net.koodar.suite.admin.exception.ServiceException;
import net.koodar.suite.admin.module.system.repository.UserRepository;
import net.koodar.suite.admin.module.system.service.UserService;
import net.koodar.suite.admin.module.system.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import net.koodar.suite.admin.module.system.model.entity.User;
import net.koodar.suite.admin.module.system.model.entity.UserRole;
import net.koodar.suite.admin.module.system.model.params.UserParam;
import net.koodar.suite.admin.module.system.model.params.UserQuery;

import jakarta.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Service Impl.
 *
 * @author liyc
 */
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserRoleService userRoleService;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userRoleService = userRoleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User loadUserById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}
		throw new UsernameNotFoundException(String.format("UserId [%s] not found in db", userId));
	}

	@Override
	public Page<User> pageBy(UserQuery userQuery, Pageable pageable) {
		Assert.notNull(userQuery, "User query must not be null");
		Assert.notNull(pageable, "Page info must not be null");

		// Build specification and find all
		return userRepository.findAll(buildSpecByQuery(userQuery), pageable);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addUser(UserParam userParam) {
		Optional<User> optionalUser = userRepository.findByUsername(userParam.getUsername());
		if (optionalUser.isPresent()) {
			throw new ServiceException(String.format("用户名 [%s] 已存在，请更换后再试吧。", userParam.getUsername()));
		}
		User user = new User();
		user.setUsername(userParam.getUsername());
		user.setNickname(userParam.getNickname());
		user.setEmail(userParam.getEmail());
		user.setAvatar(userParam.getAvatar());
		user.setDescription(userParam.getDescription());
		if (StringUtils.hasLength(userParam.getPassword())) {
			String encodePassword = passwordEncoder.encode(userParam.getPassword());
			user.setPassword(encodePassword);
		}
		userRepository.save(user);

		List<UserRole> userRoles = userParam.getRoleIds().stream().map(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			return userRole;
		}).collect(Collectors.toList());
		userRoleService.saveOrUpdate(userRoles);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateUser(UserParam userParam) {
		User user = this.loadUserById(userParam.getUserId());
		user.setNickname(userParam.getNickname());
		user.setEmail(userParam.getEmail());
		if (StringUtils.hasLength(userParam.getPassword())) {
			String encodePassword = passwordEncoder.encode(userParam.getPassword());
			user.setPassword(encodePassword);
		}
		// 更新用户信息
		userRepository.save(user);
		// 更新用户角色信息
		userRoleService.bindRoleWithUser(user.getId(), userParam.getRoleIds());
	}

	@Override
	public void deleteUser(Long id) {
		User user = this.loadUserById(id);
		user.setDeletedFlag(true);
		userRepository.save(user);
	}

	@Override
	public void updatePassword(Long userId, String oldPassword, String newPassword) {
		User user = this.loadUserById(userId);
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new ServiceException("原密码错误，请重新输入。");
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	@NonNull
	private Specification<User> buildSpecByQuery(UserQuery userQuery) {
		Assert.notNull(userQuery, "User query must not be null");
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new LinkedList<>();

			if (StringUtils.hasLength(userQuery.getUsername())) {
				predicates.add(
						criteriaBuilder.like(root.get("username"), userQuery.getUsername() + "%"));
			}

			predicates.add(criteriaBuilder.equal(root.get("deletedFlag"), false));

			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		};
	}

}
