package com.kakaopay.divider.user;

import com.kakaopay.divider.domain.jooq.tables.pojos.JUser;
import com.kakaopay.divider.user.service.UserService;
import com.kakaopay.divider.user.vo.UserRequest;
import com.kakaopay.divider.user.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 사용자 가입 처리
     *  - 임시로 name만 입력받아 생성
     * @param userRequest  - 생성 요청 정보
     * @return
     */
    @PostMapping({"", "/"})
    public UserResponse userSignup(@Valid @RequestBody UserRequest userRequest) {
        JUser user = userService.insertUser(userRequest);
        return new UserResponse(user);
    }
}
