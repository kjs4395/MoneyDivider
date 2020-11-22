package com.kakaopay.divider.user;

import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.room.vo.RoomRequest;
import com.kakaopay.divider.user.vo.UserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Random;

/**
 * Created By kjs4395 on 2020-11-23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void 사용자_생성_요청_유효성_확인() {
		UserRequest userRequest = new UserRequest();

		webTestClient.post().uri("/user")
				.bodyValue(userRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_USER_REQUEST.name());
	}

	@Test
	public void 사용자_생성_확인() {
		UserRequest userRequest = new UserRequest();
		userRequest.setName("test user - "+ new Random().nextInt(9999));

		webTestClient.post().uri("/user")
				.bodyValue(userRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(true)
				.jsonPath("$.user.name").isEqualTo(userRequest.getName());
	}
}
