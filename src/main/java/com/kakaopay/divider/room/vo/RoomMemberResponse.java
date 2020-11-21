package com.kakaopay.divider.room.vo;

import com.kakaopay.divider.common.vo.ApiResponse;
import com.kakaopay.divider.domain.jooq.tables.pojos.JRoomMember;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@RequiredArgsConstructor
public class RoomMemberResponse extends ApiResponse {
    private final JRoomMember roomMember;
}
