package run.bottle.bottleadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import run.bottle.bottleadmin.model.entity.User;

import java.util.Optional;

/**
 * User repository.
 *
 * @author liyc
 * @date 2022-09-04
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Gets user by username.
	 * @param username username must not be blank
	 * @return an optional user
	 */
	@NonNull
	Optional<User> findByUsername(@NonNull String username);

	/**
	 * Gets user by email.
	 *
	 * @param email email must not be blank
	 * @return an optional user
	 */
	@NonNull
	Optional<User> findByEmail(@NonNull String email);
}
