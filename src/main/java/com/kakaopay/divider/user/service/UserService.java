package com.kakaopay.divider.user.service;

import com.kakaopay.divider.domain.jooq.tables.pojos.JUser;
import com.kakaopay.divider.user.dao.UserDao;
import com.kakaopay.divider.user.vo.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDao userDao;

    public JUser insertUser(UserRequest userRequest) {
        JUser user = new JUser();
        user.setName(userRequest.getName());
        user.setCreateDate(LocalDateTime.now());
        userDao.insert(user);
        return user;
    }
}
