package com.kakaopay.divider.room.service;

import com.kakaopay.divider.domain.jooq.tables.pojos.JRoom;
import com.kakaopay.divider.domain.jooq.tables.pojos.JRoomMember;
import com.kakaopay.divider.room.dao.RoomDao;
import com.kakaopay.divider.room.dao.RoomMemberDao;
import com.kakaopay.divider.room.vo.RoomMemberRequest;
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
    private final RoomMemberDao roomMemberDao;

    public JRoom insertRoom(RoomRequest roomRequest) {
        JRoom room = new JRoom();
        room.setName(roomRequest.getName());
        room.setCreateDate(LocalDateTime.now());
        roomDao.insert(room);
        return room;
    }

    public JRoomMember insertRoomMember(RoomMemberRequest roomMemberRequest) {
        JRoomMember roomMember = new JRoomMember();
        roomMember.setRoomId(roomMemberRequest.getRoomId());
        roomMember.setUserId(roomMemberRequest.getUserId());
        roomMember.setJoinDate(LocalDateTime.now());
        roomMemberDao.insert(roomMember);
        return roomMember;
    }
}
