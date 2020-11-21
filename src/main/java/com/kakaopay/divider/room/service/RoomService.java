package com.kakaopay.divider.room.service;

import com.kakaopay.divider.domain.jooq.tables.pojos.JRoom;
import com.kakaopay.divider.room.dao.RoomDao;
import com.kakaopay.divider.room.vo.RoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomDao roomDao;

    public JRoom insertRoom(RoomRequest roomRequest) {
        JRoom room = new JRoom();
        room.setName(roomRequest.getName());
        room.setCreateDate(LocalDateTime.now());
        roomDao.insert(room);
        return room;
    }
}
