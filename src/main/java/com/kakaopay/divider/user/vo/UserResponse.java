package com.kakaopay.divider.user.vo;

import com.kakaopay.divider.common.vo.ApiResponse;
import com.kakaopay.divider.domain.jooq.tables.pojos.JUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Getter
public class UserResponse extends ApiResponse {
    private final JUser user;
}
