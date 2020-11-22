package com.kakaopay.divider.room.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@Setter
public class RoomMemberRequest {
    @NotNull
    private Integer roomId;
    @NotNull
    private Integer userId;
}
