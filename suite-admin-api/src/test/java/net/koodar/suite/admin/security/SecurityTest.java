package net.koodar.suite.admin.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import net.koodar.suite.common.util.JsonUtils;

import java.util.Map;

/**
 * Security test
 *
 * @author liyc
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void indexWhenUnAuthenticatedThenSucceed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void indexWhenUnAuthenticatedThenFailed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/info"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	void login() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "user")
				.param("password", "123456"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void requestWithAuthenticated() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "user")
				.param("password", "123456"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse();
		Map resultMap = JsonUtils.jsonToObject(response.getContentAsString(), Map.class);
		String accessToken = (String) ((Map) resultMap.get("data")).get("access_token");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/info")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


	@Test
	void requestWhenAuthenticatedThenForbidden() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "user")
				.param("password", "123456"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse();
		Map resultMap = JsonUtils.jsonToObject(response.getContentAsString(), Map.class);
		String accessToken = (String) ((Map) resultMap.get("data")).get("access_token");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	void requestWhenAuthenticatedThenSucceed() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "admin")
				.param("password", "123456"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse();
		Map resultMap = JsonUtils.jsonToObject(response.getContentAsString(), Map.class);
		String accessToken = (String) ((Map) resultMap.get("data")).get("access_token");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void logout() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "admin")
				.param("password", "123456"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse();
		Map resultMap = JsonUtils.jsonToObject(response.getContentAsString(), Map.class);
		String accessToken = (String) ((Map) resultMap.get("data")).get("access_token");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/info")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/logout")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/info")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());


	}

}
