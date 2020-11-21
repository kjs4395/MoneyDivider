package com.kakaopay.divider.money.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JMoneyDao;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyState;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.kakaopay.divider.domain.jooq.Tables.MONEY;

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

    public boolean isTimeout(MoneyDivideRequest moneyDivideRequest) {
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
}
