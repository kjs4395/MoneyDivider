package com.kakaopay.divider.money.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JMoneyDao;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.kakaopay.divider.domain.jooq.Tables.*;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Repository
public class MoneyDao extends JMoneyDao {
    private final DSLContext context;

    public MoneyDao(Configuration configuration,
                    DSLContext context) {
        super(configuration);
        this.context = context;
    }

    public boolean isOwner(MoneyDivideRequest moneyDivideRequest) {
        return context.select(MONEY.OWNER_ID)
                .from(MONEY)
                .where(MONEY.ROOM_ID.eq(moneyDivideRequest.getId().getRoomId()))
                .and(MONEY.TOKEN.eq(moneyDivideRequest.getToken()))
                .and(MONEY.OWNER_ID.eq(moneyDivideRequest.getId().getUserId()))
                .fetchOne(MONEY.OWNER_ID) != null;
    }

    public boolean isExpired(MoneyDivideRequest moneyDivideRequest) {
        return context.select(MONEY.ROOM_ID)
                .from(MONEY)
                .where(MONEY.ROOM_ID.eq(moneyDivideRequest.getId().getRoomId()))
                .and(MONEY.TOKEN.eq(moneyDivideRequest.getToken()))
                .and(MONEY.STATE.ne(MoneyState.COMPLETE))
                .and(MONEY.CREATE_DATE.ge(LocalDateTime.now().minusMinutes(10)))
                .fetchOne(MONEY.ROOM_ID) == null;
    }

    public int updateMoneyState(int roomId, String token, MoneyState moneyState) {
        return context.update(MONEY)
                .set(MONEY.STATE, moneyState)
                .where(MONEY.ROOM_ID.eq(roomId))
                .and(MONEY.TOKEN.eq(token))
                .and(MONEY.STATE.ne(moneyState))
                .execute();
    }

    public Record findMoneyInfoByOwnerId(MoneyRequestId moneyRequestId, String token) {
        return context
                .select(MONEY.asterisk(),
                        ROOM.NAME,
                        context.select(DSL.sum(MONEY_DIVIDE_INFO.AMOUNT))
                                .from(MONEY_DIVIDE_INFO)
                                .where(MONEY_DIVIDE_INFO.MONEY_ROOM_ID.eq(MONEY.ROOM_ID))
                                .and(MONEY_DIVIDE_INFO.MONEY_TOKEN.eq(MONEY.TOKEN))
                                .and(MONEY_DIVIDE_INFO.RECEIVE_USER_ID.isNotNull())
                                .asField("receiveAmount")
                )
                .from(MONEY)
                .innerJoin(ROOM).on(ROOM.ID.eq(MONEY.ROOM_ID))
                .where(MONEY.ROOM_ID.eq(moneyRequestId.getRoomId()))
                .and(MONEY.TOKEN.eq(token))
                .and(MONEY.OWNER_ID.eq(moneyRequestId.getUserId()))
                .and(MONEY.CREATE_DATE.ge(LocalDateTime.now().minusDays(7)))
                .fetchOne();
    }
}
