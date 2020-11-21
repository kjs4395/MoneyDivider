package com.kakaopay.divider.room.vo;

import com.kakaopay.divider.common.vo.ApiResponse;
import com.kakaopay.divider.domain.jooq.tables.pojos.JRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@RequiredArgsConstructor
public class RoomResponse extends ApiResponse {
    private final JRoom room;
}
