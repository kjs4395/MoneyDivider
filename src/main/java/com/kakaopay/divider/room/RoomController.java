package com.kakaopay.divider.room;

import com.kakaopay.divider.domain.jooq.tables.pojos.JRoom;
import com.kakaopay.divider.domain.jooq.tables.pojos.JRoomMember;
import com.kakaopay.divider.room.service.RoomService;
import com.kakaopay.divider.room.vo.RoomMemberRequest;
import com.kakaopay.divider.room.vo.RoomMemberResponse;
import com.kakaopay.divider.room.vo.RoomRequest;
import com.kakaopay.divider.room.vo.RoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequestMapping("/room")
@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    /**
     * 뿌리기 가능한 룸 생성
     *   - 임시로 name만 요청받아 생성
     * @param roomRequest  - 생성 요청 정보
     * @return
     */
    @PostMapping({"", "/"})
    public RoomResponse createRoom(@Valid @RequestBody RoomRequest roomRequest) {
        JRoom room = roomService.insertRoom(roomRequest);
        return new RoomResponse(room);
    }

    /**
     * 뿌리기 룸 입장 처리
     * @param roomMemberRequest  - 룸 입장 요청 정보
     * @return
     */
    @PostMapping("/member")
    public RoomMemberResponse joinRoom(@Valid @RequestBody RoomMemberRequest roomMemberRequest) {
        JRoomMember roomMember = roomService.insertRoomMember(roomMemberRequest);
        return new RoomMemberResponse(roomMember);
    }
}
