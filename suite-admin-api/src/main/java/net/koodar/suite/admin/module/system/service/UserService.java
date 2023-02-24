package net.koodar.suite.admin.module.system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import net.koodar.suite.admin.module.system.model.entity.User;
import net.koodar.suite.admin.module.system.model.params.UserParam;
import net.koodar.suite.admin.module.system.model.params.UserQuery;

/**
 * User Service.
 *
 * @author liyc
 */
public interface UserService {

	User loadUserById(@NonNull Long userId);

	Page<User> pageBy(@NonNull UserQuery userQuery, @NonNull Pageable pageable);

	void addUser(UserParam userParam);

	void updateUser(UserParam userParam);

	void deleteUser(Long id);

	void updatePassword(Long userId, String oldPassword, String newPassword);

}
