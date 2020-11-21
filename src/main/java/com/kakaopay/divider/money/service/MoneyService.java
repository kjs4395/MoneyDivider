package com.kakaopay.divider.money.service;

import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.money.dao.MoneyDao;
import com.kakaopay.divider.money.dao.MoneySeqDao;
import com.kakaopay.divider.money.util.TokenGenerator;
import com.kakaopay.divider.money.vo.MoneyRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Service
public class MoneyService {
    private final MoneyDao moneyDao;
    private final MoneySeqDao moneySeqDao;
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
}
