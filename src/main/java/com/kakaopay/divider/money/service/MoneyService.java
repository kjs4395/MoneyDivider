package com.kakaopay.divider.money.service;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import com.kakaopay.divider.domain.jooq.tables.pojos.JUser;
import com.kakaopay.divider.money.dao.MoneyDao;
import com.kakaopay.divider.money.dao.MoneyDivideInfoDao;
import com.kakaopay.divider.money.dao.MoneySeqDao;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import com.kakaopay.divider.money.util.TokenGenerator;
import com.kakaopay.divider.money.vo.MoneyInfoReseponse;
import com.kakaopay.divider.money.vo.MoneyRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Service
public class MoneyService {
    private final MoneyDao moneyDao;
    private final MoneySeqDao moneySeqDao;
    private final MoneyDivideInfoDao moneyDivideInfoDao;
    private final MoneyDivideInfoService moneyDivideInfoService;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public JMoney generateMoneyForDivide(MoneyRequest moneyRequest) {
        JMoney money = this.insertMoney(moneyRequest);
        moneyDivideInfoService.insertMoneyDivideInfos(money, moneyRequest.getDivideNum());
        moneySeqDao.generateMoneySeq(money, moneyRequest.getDivideNum());
        return money;
    }

    public JMoney insertMoney(MoneyRequest moneyRequest) {
        JMoney money = new JMoney();
        money.setRoomId(moneyRequest.getId().getRoomId());
        money.setOwnerId(moneyRequest.getId().getUserId());
        money.setAmount(moneyRequest.getAmount());
        money.setCreateDate(LocalDateTime.now());
        money.setToken(tokenGenerator.generateToken());
        money.setState(MoneyState.CREATED);
        moneyDao.insert(money);
        return money;
    }

    public MoneyInfoReseponse.MoneyInfo getMoneyInfo(MoneyRequestId moneyRequestId, String token) {
        Record moneyRecord = moneyDao.findMoneyInfoByOwnerId(moneyRequestId, token);
        if(moneyRecord == null) {
            throw new ApiException(ApiStatusCode.MONEY_NOT_FOUND);
        }

        JMoney money = moneyRecord.into(JMoney.class);
        List<Record> receiveUserRecordList = moneyDivideInfoDao.findListByMoney(money);
        List<MoneyInfoReseponse.ReceiveUser> receiveUserList = receiveUserRecordList
                .stream()
                .map(receiveUserRecord -> {
                    JMoneyDivideInfo moneyDivideInfo = receiveUserRecord.into(JMoneyDivideInfo.class);
                    JUser user = receiveUserRecord.into(JUser.class);

                    return MoneyInfoReseponse.ReceiveUser.builder()
                            .userId(moneyDivideInfo.getReceiveUserId())
                            .userName(user.getName())
                            .amount(moneyDivideInfo.getAmount())
                            .receiveDate(moneyDivideInfo.getReceiveDate())
                            .build();
                })
                .collect(Collectors.toList());

        return MoneyInfoReseponse.MoneyInfo.builder()
                .roomId(moneyRequestId.getRoomId())
                .token(token)
                .amount(money.getAmount())
                .receiveAmount(moneyRecord.get("receiveAmount", Integer.class))
                .state(money.getState())
                .createDate(money.getCreateDate())
                .receiveUserList(receiveUserList)
                .build();
    }
}
