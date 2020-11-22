package com.kakaopay.divider.money;

import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.money.vo.MoneyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created By kjs4395 on 2020-11-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyRequestTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final String X_ROOM_ID = "X-ROOM-ID";
    private static final String X_USER_ID = "X-USER-ID";

    @Test
    public void 뿌리기_요청_ID_유효성_확인() {
        webTestClient.post().uri("/money")
                .header(X_ROOM_ID, "")
                .header(X_USER_ID, "")
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.result.success").isEqualTo(false)
                .jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_MONEY_REQUEST_ID.name());
    }

    @Test
    public void 뿌리기_요청_Body값_유효성_확인() {
        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setAmount(-1);
        moneyRequest.setDivideNum(-1);

        webTestClient.post().uri("/money")
                .header(X_ROOM_ID, "1")
                .header(X_USER_ID, "1")
                .bodyValue(moneyRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.result.success").isEqualTo(false)
                .jsonPath("$.result.status").isEqualTo(ApiStatusCode.INVALID_MONEY_REQUEST.name());
    }

    @Test
    public void 뿌리기_요청_룸_멤버_유효성_확인() {
        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setAmount(10000);
        moneyRequest.setDivideNum(3);

        webTestClient.post().uri("/money")
                .header(X_ROOM_ID, "1")
                .header(X_USER_ID, "9999")
                .bodyValue(moneyRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.result.success").isEqualTo(false)
                .jsonPath("$.result.status").isEqualTo(ApiStatusCode.NOT_IN_ROOM_MEMBER.name());
    }

    @Test
    public void 뿌리기_요청_성공_후_토큰_생성_확인() {
        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setAmount(10000);
        moneyRequest.setDivideNum(3);

        webTestClient.post().uri("/money")
                .header(X_ROOM_ID, "1")
                .header(X_USER_ID, "1")
                .bodyValue(moneyRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.result.success").isEqualTo(true)
                .jsonPath("$.money.token").isNotEmpty();
    }
}
