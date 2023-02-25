package net.koodar.suite.admin.module.system.user.service;

import lombok.RequiredArgsConstructor;
import net.koodar.suite.admin.exception.ServiceException;
import net.koodar.suite.admin.module.system.user.manager.UserRoleManager;
import net.koodar.suite.admin.module.system.user.repository.UserRepository;
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
import net.koodar.suite.admin.module.system.user.domain.User;
import net.koodar.suite.admin.module.system.role.domain.UserRole;
import net.koodar.suite.admin.module.system.user.domain.UserParam;
import net.koodar.suite.admin.module.system.user.domain.UserQuery;

import jakarta.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Service.
 *
 * @author liyc
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserRoleManager userRoleManager;
	private final PasswordEncoder passwordEncoder;

	public User loadUserById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}
		throw new UsernameNotFoundException(String.format("UserId [%s] not found in db", userId));
	}

	public Page<User> pageBy(UserQuery userQuery, Pageable pageable) {
		Assert.notNull(userQuery, "User query must not be null");
		Assert.notNull(pageable, "Page info must not be null");

		// Build specification and find all
		return userRepository.findAll(buildSpecByQuery(userQuery), pageable);
	}

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
		// 更新用户和角色信息
		userRoleManager.updateUserRole(user, userParam.getRoleIds());
	}

	public void updateUser(UserParam userParam) {
		User user = this.loadUserById(userParam.getUserId());
		user.setNickname(userParam.getNickname());
		user.setEmail(userParam.getEmail());
		if (StringUtils.hasLength(userParam.getPassword())) {
			String encodePassword = passwordEncoder.encode(userParam.getPassword());
			user.setPassword(encodePassword);
		}
		// 更新用户和角色信息
		userRoleManager.updateUserRole(user, userParam.getRoleIds());
	}

	public void deleteUser(Long id) {
		User user = this.loadUserById(id);
		user.setDeletedFlag(true);
		userRepository.save(user);
	}

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
