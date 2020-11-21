package com.kakaopay.divider.money.dao;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created By kjs4395 on 2020-11-22
 */
@RequiredArgsConstructor
@Repository
public class MoneySeqDao {
	private final RedisTemplate<String, Object> redisTemplate;

	public void generateMoneySeq(JMoney money, int seqCount) {
		String key = this.getMoneySeqKey(money);
		redisTemplate.opsForList().rightPushAll(key, IntStream.rangeClosed(1, seqCount).boxed().toArray());
		redisTemplate.expire(key, 10, TimeUnit.MINUTES);
	}

	public int nextMoneySeq(MoneyDivideRequest moneyDivideRequest) {
		String key = this.getMoneySeqKey(moneyDivideRequest);

		Boolean hasKey = redisTemplate.hasKey(key);
		if(hasKey == null || !hasKey) {
			throw new ApiException("받을 수 있는 금액이 없습니다.");
		}

		Integer moneySeq = (Integer) redisTemplate.opsForList().leftPop(key);
		if(moneySeq == null) {
			throw new ApiException("받을 수 있는 금액이 없습니다.");
		}
		return moneySeq;
	}

	public void restoreMoneySeq(MoneyDivideRequest moneyDivideRequest, int seq) {
		String key = this.getMoneySeqKey(moneyDivideRequest);
		redisTemplate.opsForList().leftPush(key, seq);
	}

	public long getRemainCount(MoneyDivideRequest moneyDivideRequest) {
		String key = this.getMoneySeqKey(moneyDivideRequest);
		Long size = redisTemplate.opsForList().size(key);
		return (size == null)? 0 : size;
	}


	private String getMoneySeqKey(JMoney money) {
		return this.getKey(
				String.valueOf(money.getRoomId()),
				money.getToken()
		);
	}

	private String getMoneySeqKey(MoneyDivideRequest moneyDivideRequest) {
		return this.getKey(
				String.valueOf(moneyDivideRequest.getId().getRoomId()),
				moneyDivideRequest.getToken()
		);
	}

	private String getKey(String ...args) {
		LinkedList<String> keys = new LinkedList<>(Arrays.asList(args));
		keys.addFirst("MoneySeq");
		return String.join(":", keys);
	}
}
