package com.kakaopay.divider.money.interceptor;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-11-21
 */
public class MoneyRequestInterceptor extends HandlerInterceptorAdapter {
	private static final String X_ROOM_ID = "X-ROOM-ID";
	private static final String X_USER_ID = "X-USER-ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String userId = request.getHeader(X_USER_ID);
		String roomId = request.getHeader(X_ROOM_ID);

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(roomId)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			throw new ApiException("ID 정보가 없습니다.");
		}

		MoneyRequestId moneyRequestId = new MoneyRequestId(Integer.valueOf(roomId), Integer.valueOf(userId));
		request.setAttribute("moneyRequestId", moneyRequestId);
		return true;
	}
}
