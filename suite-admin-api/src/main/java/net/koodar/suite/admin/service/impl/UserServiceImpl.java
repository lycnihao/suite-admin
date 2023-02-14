package net.koodar.suite.admin.service.impl;

import net.koodar.suite.admin.exception.ServiceException;
import net.koodar.suite.admin.repository.UserRepository;
import net.koodar.suite.admin.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import net.koodar.suite.admin.model.entity.User;
import net.koodar.suite.admin.model.entity.UserRole;
import net.koodar.suite.admin.model.params.UserParam;
import net.koodar.suite.admin.model.params.UserQuery;
import net.koodar.suite.admin.service.UserService;

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

	public UserServiceImpl(UserRepository userRepository, UserRoleService userRoleService) {
		this.userRepository = userRepository;
		this.userRoleService = userRoleService;
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
			String encodePassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userParam.getPassword());
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
		Optional<User> optionalUser = userRepository.findById(userParam.getUserId());
		if (!optionalUser.isPresent()) {
			throw new ServiceException(String.format("userId [%s] not found in db", userParam.getUserId()));
		}
		User user = optionalUser.get();
		user.setNickname(userParam.getNickname());
		user.setEmail(userParam.getEmail());
		if (StringUtils.hasLength(userParam.getPassword())) {
			String encodePassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userParam.getPassword());
			user.setPassword(encodePassword);
		}
		// 更新用户信息
		userRepository.save(user);
		// 更新用户角色信息
		userRoleService.bindRoleWithUser(user.getId(), userParam.getRoleIds());
	}

	@Override
	public void deleteUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new ServiceException(String.format("userId [%s] not found in db", id));
		}
		User user = optionalUser.get();
		user.setDeletedFlag(true);
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
