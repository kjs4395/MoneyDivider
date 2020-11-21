package com.kakaopay.divider.room.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JRoomDao;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.kakaopay.divider.domain.jooq.Tables.ROOM_MEMBER;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Repository
public class RoomDao extends JRoomDao {
    private final DSLContext context;

    public RoomDao(Configuration configuration,
                   DSLContext context) {
        super(configuration);
        this.context = context;
    }

    public boolean isRoomMember(MoneyRequestId id) {
        return this.context.select(ROOM_MEMBER.USER_ID)
                .from(ROOM_MEMBER)
                .where(ROOM_MEMBER.ROOM_ID.eq(id.getRoomId()))
                .and(ROOM_MEMBER.USER_ID.eq(id.getUserId()))
                .fetchOne(ROOM_MEMBER.USER_ID) != null;
    }
}
