package com.kakaopay.divider.money.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JMoneyDivideInfoDao;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.kakaopay.divider.domain.jooq.Tables.MONEY_DIVIDE_INFO;
import static com.kakaopay.divider.domain.jooq.Tables.USER;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Repository
public class MoneyDivideInfoDao extends JMoneyDivideInfoDao {
    private final DSLContext context;

    public MoneyDivideInfoDao(Configuration configuration,
                              DSLContext context) {
        super(configuration);
        this.context = context;
    }

    public JMoneyDivideInfo findByMoneyDivideRequest(MoneyDivideRequest moneyDivideRequest, int seq) {
        return this.findById(
                context.newRecord(
                        MONEY_DIVIDE_INFO.MONEY_ROOM_ID,
                        MONEY_DIVIDE_INFO.MONEY_TOKEN,
                        MONEY_DIVIDE_INFO.SEQ
                ).values(
                        moneyDivideRequest.getId().getRoomId(),
                        moneyDivideRequest.getToken(),
                        seq
                )
        );
    }

    public Result<Record> findListByMoney(JMoney money) {
        return context.select()
                .from(MONEY_DIVIDE_INFO)
                .innerJoin(USER).on(USER.ID.eq(MONEY_DIVIDE_INFO.RECEIVE_USER_ID))
                .where(MONEY_DIVIDE_INFO.MONEY_ROOM_ID.eq(money.getRoomId()))
                .and(MONEY_DIVIDE_INFO.MONEY_TOKEN.eq(money.getToken()))
                .orderBy(MONEY_DIVIDE_INFO.SEQ.asc())
                .fetch();
    }

    public boolean isReceivedUser(MoneyDivideRequest moneyDivideRequest) {
        return this.context.select(MONEY_DIVIDE_INFO.RECEIVE_USER_ID)
                .from(MONEY_DIVIDE_INFO)
                .where(MONEY_DIVIDE_INFO.MONEY_ROOM_ID.eq(moneyDivideRequest.getId().getRoomId()))
                .and(MONEY_DIVIDE_INFO.MONEY_TOKEN.eq(moneyDivideRequest.getToken()))
                .and(MONEY_DIVIDE_INFO.RECEIVE_USER_ID.eq(moneyDivideRequest.getId().getUserId()))
                .fetchOne(MONEY_DIVIDE_INFO.RECEIVE_USER_ID) != null;
    }

    public int updateReceiveMoneyDivide(MoneyDivideRequest moneyDivideRequest, int seq) {
        LocalDateTime now = LocalDateTime.now();
        return this.context.update(MONEY_DIVIDE_INFO)
                .set(MONEY_DIVIDE_INFO.RECEIVE_USER_ID, moneyDivideRequest.getId().getUserId())
                .set(MONEY_DIVIDE_INFO.RECEIVE_DATE, now)
                .where(MONEY_DIVIDE_INFO.MONEY_ROOM_ID.eq(moneyDivideRequest.getId().getRoomId()))
                .and(MONEY_DIVIDE_INFO.MONEY_TOKEN.eq(moneyDivideRequest.getToken()))
                .and(MONEY_DIVIDE_INFO.SEQ.eq(seq))
                .and(MONEY_DIVIDE_INFO.RECEIVE_USER_ID.isNull())
                .and(MONEY_DIVIDE_INFO.RECEIVE_DATE.isNull())
                .and(MONEY_DIVIDE_INFO.CREATE_DATE.ge(now.minusMinutes(10)))
                .execute();
    }
}
