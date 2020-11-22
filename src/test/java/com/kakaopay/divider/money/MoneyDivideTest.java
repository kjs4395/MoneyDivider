package com.kakaopay.divider.money;

import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.money.util.MoneyRequestId;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyRequest;
import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

/**
 * Created By kjs4395 on 2020-11-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyDivideTest {
	@Autowired
	private WebTestClient webTestClient;
	private static String token;
	private static boolean isReady;

	private static final String ROOM_ID = "1";
	private static final String OWNER_ID = "1";

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
				.jsonPath("$.money.token").value(token -> MoneyDivideTest.token = (String) token);
	}

	@Test
	public void 뿌린_금액_받기_ID_유효성_확인() {
		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, "")
				.header(MoneyRequestId.X_USER_ID, "")
				.exchange()
				.expectStatus().isForbidden()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_MONEY_REQUEST_ID.name());
	}

	@Test
	public void 뿌린_금액_받기_Body값_유효성_확인() {
		MoneyDivideRequest moneyDivideRequest = new MoneyDivideRequest();
		moneyDivideRequest.setToken("invalidToken");

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "2")
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_MONEY_DIVIDE_REQUEST.name());
	}

	@Test
	public void 뿌린_금액_받기_뿌리기_당_한_사용자만_허용_확인() {
		MoneyDivideRequest moneyDivideRequest = new MoneyDivideRequest();
		moneyDivideRequest.setToken(token);

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "2")
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(true)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.SUCCESS.name());

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "2")
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.ALREADY_RECEIVE_USER.name());
	}

	@Test
	public void 뿌린_금액_받기_자신이_뿌리기한_건은_자신이_받을_수_없는지_확인() {
		MoneyDivideRequest moneyDivideRequest = new MoneyDivideRequest();
		moneyDivideRequest.setToken(token);

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, OWNER_ID)
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.NOT_ALLOWED_DIVIDE_BY_OWNER.name());
	}

	@Test
	public void 뿌린_금액_받기_뿌리기가_호출된_대화방과_동일한_대화방에_속한_사용자만_허용_확인() {
		MoneyDivideRequest moneyDivideRequest = new MoneyDivideRequest();
		moneyDivideRequest.setToken(token);

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "9999")
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.NOT_IN_ROOM_MEMBER.name());
	}

	@Test
	public void 뿌린_금액_받기_뿌린지_10분이_지난_요청_유효성_확인() {
		MoneyDivideRequest moneyDivideRequest = new MoneyDivideRequest();
		moneyDivideRequest.setToken(token);

		LocalDateTime dateTimeAfter10Min = LocalDateTime.now().plusMinutes(10).plusSeconds(1);
		new Expectations(LocalDateTime.class) {
			{
				LocalDateTime.now();
				result = dateTimeAfter10Min;
			}
		};

		webTestClient.put().uri("/money/divide")
				.header(MoneyRequestId.X_ROOM_ID, ROOM_ID)
				.header(MoneyRequestId.X_USER_ID, "3")
				.bodyValue(moneyDivideRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.result.success").isEqualTo(false)
				.jsonPath("$.result.status").isEqualTo(ApiStatusCode.MONEY_EXPIRED.name());
	}
}
