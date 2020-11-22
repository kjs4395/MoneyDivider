package com.kakaopay.divider.money;

import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.money.util.MoneyRequestId;
import com.kakaopay.divider.money.vo.MoneyRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Created By kjs4395 on 2020-11-23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyInfoTest {
	@Autowired
	private WebTestClient webTestClient;
	private static String token;
	private static boolean isReady;

	private static final String ROOM_ID = "1";
	private static final String OWNER_ID = "1";

	private String encodeUrl(String s) {
		try {
			return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	@Before
	public void 뿌리기_등록_토큰_생성() {
		if(isReady) return;
		MoneyRequest moneyRequest = new MoneyRequest();
		moneyRequest.setAmount(10000);
		moneyRequest.setDivideNum(3);

		isReady = true;
		webTestClient.post().uri("/money")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, OWNER_ID)
				.bodyValue(moneyRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(true)
				.jsonPath("$.money.token").value(token -> MoneyInfoTest.token = encodeUrl((String) token));
	}

	@Test
	public void 뿌리기_조회_ID_유효성_확인() {
		webTestClient.get().uri("/money/"+ token)
				.header(MoneyRequestId.X_ROOM_ID, "")
				.header(MoneyRequestId.X_USER_ID, "")
				.exchange()
				.expectStatus().isForbidden()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_MONEY_REQUEST_ID.name());
	}

	@Test
	public void 뿌리기_조회_토큰_유효성_확인() {
		webTestClient.get().uri("/money/invalidToken")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, OWNER_ID)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_TOKEN.name());
	}

	@Test
	public void 뿌리기_조회_생성_상태_확인() {
		webTestClient.get().uri("/money/"+ token)
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, OWNER_ID)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(true)
				.jsonPath("$.moneyInfo.state").isEqualTo(MoneyState.CREATED.name());
	}

	@Test
	public void 뿌리기_조회_다른_사람이_조회시_오류_확인() {
		webTestClient.get().uri("/money/"+ token)
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "2")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.MONEY_NOT_FOUND.name());
	}

	@Test
	public void 뿌리기_조회_7일이_지난_뿌리기_조회시_오류_확인() {
		LocalDateTime dateTimeAfter7Days = LocalDateTime.now().plusDays(7).plusSeconds(1);
		new Expectations(LocalDateTime.class) {
			{
				LocalDateTime.now();
				result = dateTimeAfter7Days;
			}
		};

		webTestClient.get().uri("/money/"+ token)
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, OWNER_ID)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.MONEY_NOT_FOUND.name());
	}
}
