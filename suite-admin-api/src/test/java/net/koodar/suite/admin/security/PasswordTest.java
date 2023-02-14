package net.koodar.suite.admin.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder test
 *
 * @author liyc
 */
public class PasswordTest {

	@Test
	void encoderPasswordWithDefaultEncoder() {
		PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encodePassword = delegatingPasswordEncoder.encode("123456");
		boolean matches = delegatingPasswordEncoder.matches("123456", encodePassword);
		Assertions.assertTrue(matches);
	}

	@Test
	void defaultEncoderEncoderPasswordThenFailed() {
		PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encodePassword = delegatingPasswordEncoder.encode("123456");
		boolean matches = delegatingPasswordEncoder.matches("123abc", encodePassword);
		Assertions.assertFalse(matches);
	}

}
