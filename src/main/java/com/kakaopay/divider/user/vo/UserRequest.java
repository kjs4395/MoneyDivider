package com.kakaopay.divider.user.vo;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
public class UserRequest {
    @NotBlank
    @Size(max = 20)
    private String name;
}
