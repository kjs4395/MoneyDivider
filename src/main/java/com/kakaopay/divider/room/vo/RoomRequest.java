package com.kakaopay.divider.room.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@Setter
public class RoomRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
}
