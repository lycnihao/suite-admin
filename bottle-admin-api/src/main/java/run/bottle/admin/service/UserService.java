package run.bottle.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import run.bottle.admin.model.entity.User;
import run.bottle.admin.model.params.UserParam;
import run.bottle.admin.model.params.UserQuery;

/**
 * User Service.
 *
 * @author liyc
 * @date 2022-10-26
 */
public interface UserService {

	User loadUserById(@NonNull Long userId);

	Page<User> pageBy(@NonNull UserQuery userQuery, @NonNull Pageable pageable);

	void addUser(UserParam userParam);

	void updateUser(UserParam userParam);

	void deleteUser(Long id);

}
