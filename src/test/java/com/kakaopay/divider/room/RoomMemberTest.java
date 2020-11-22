package com.kakaopay.divider.room;

import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.room.vo.RoomMemberRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created By kjs4395 on 2020-11-23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomMemberTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void 룸_입장_요청_유효성_확인() {
		RoomMemberRequest roomMemberRequest = new RoomMemberRequest();
		roomMemberRequest.setRoomId(null);
		roomMemberRequest.setUserId(null);

		webTestClient.post().uri("/room/member")
				.bodyValue(roomMemberRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_JOIN_ROOM_REQUEST.name());
	}

	@Test
	public void 룸_입장_중복_요청_오류_확인() {
		RoomMemberRequest roomMemberRequest = new RoomMemberRequest();
		roomMemberRequest.setRoomId(1);
		roomMemberRequest.setUserId(1);

		webTestClient.post().uri("/room/member")
				.bodyValue(roomMemberRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.FAIL.name());
	}
}
