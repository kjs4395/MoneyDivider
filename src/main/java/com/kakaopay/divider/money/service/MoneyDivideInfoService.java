package com.kakaopay.divider.money.service;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import com.kakaopay.divider.money.dao.MoneyDao;
import com.kakaopay.divider.money.dao.MoneyDivideInfoDao;
import com.kakaopay.divider.money.dao.MoneySeqDao;
import com.kakaopay.divider.money.util.MoneyDivider;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import com.kakaopay.divider.room.dao.RoomDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Service
public class MoneyDivideInfoService {
	private final RoomDao roomDao;
	private final MoneyDao moneyDao;
	private final MoneySeqDao moneySeqDao;
	private final MoneyDivideInfoDao moneyDivideInfoDao;

	public void insertMoneyDivideInfos(JMoney money, int memberCount) {
		List<Integer> divideList = MoneyDivider.divideMoney(money.getAmount(), memberCount);
		for(int i = 0; i < divideList.size(); i++) {
			this.insertMoneyDivideInfo(money, i + 1, divideList.get(i));
		}
	}

	public void insertMoneyDivideInfo(JMoney money, int seq, int amount) {
		JMoneyDivideInfo moneyDivideInfo = new JMoneyDivideInfo();
		moneyDivideInfo.setMoneyRoomId(money.getRoomId());
		moneyDivideInfo.setMoneyToken(money.getToken());
		moneyDivideInfo.setSeq(seq);
		moneyDivideInfo.setAmount(amount);
		moneyDivideInfo.setCreateDate(LocalDateTime.now());
		moneyDivideInfoDao.insert(moneyDivideInfo);
	}

	public JMoneyDivideInfo receiveMoneyDivide(MoneyDivideRequest moneyDivideRequest) {
		if(moneyDao.isTimeout(moneyDivideRequest)) {
			throw new ApiException("유효하지 않은 뿌리기입니다.");
		}

		if(moneyDao.isOwner(moneyDivideRequest)) {
			throw new ApiException("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
		}

		if(!roomDao.isRoomMember(moneyDivideRequest.getId())) {
			throw new ApiException("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.");
		}

		if(moneyDivideInfoDao.isReceivedUser(moneyDivideRequest)) {
			throw new ApiException("이미 받아간 사용자입니다.");
		}

		int seq = moneySeqDao.nextMoneySeq(moneyDivideRequest);
		int update = moneyDivideInfoDao.updateReceiveMoneyDivide(moneyDivideRequest, seq);
		if(update <= 0) {
			moneySeqDao.restoreMoneySeq(moneyDivideRequest, seq);
			throw new ApiException(
					String.format("잘못된 처리된 뿌리기 정보입니다. (roomId: %d, token: %s, seq: %d)",
							moneyDivideRequest.getId().getRoomId(),
							moneyDivideRequest.getToken(),
							seq
					)
			);
		}
		this.updateMoneyState(moneyDivideRequest);
		return moneyDivideInfoDao.findByMoneyDivideRequest(moneyDivideRequest, seq);
	}

	private void updateMoneyState(MoneyDivideRequest moneyDivideRequest) {
		MoneyState state = MoneyState.IN_PROGRESS;
		if(moneySeqDao.getRemainCount(moneyDivideRequest) == 0) {
			state = MoneyState.COMPLETE;
		}
		moneyDao.updateMoneyState(moneyDivideRequest.getId().getRoomId(), moneyDivideRequest.getToken(), state);
	}
}
