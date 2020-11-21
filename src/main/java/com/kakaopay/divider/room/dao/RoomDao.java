package com.kakaopay.divider.room.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JRoomDao;
import org.jooq.Configuration;
import org.springframework.stereotype.Repository;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Repository
public class RoomDao extends JRoomDao {

    public RoomDao(Configuration configuration) {
        super(configuration);
    }
}
