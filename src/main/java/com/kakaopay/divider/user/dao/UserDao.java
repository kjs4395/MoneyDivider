package com.kakaopay.divider.user.dao;

import com.kakaopay.divider.domain.jooq.tables.daos.JUserDao;
import org.jooq.Configuration;
import org.springframework.stereotype.Repository;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Repository
public class UserDao extends JUserDao {

    public UserDao(Configuration configuration) {
        super(configuration);
    }
}
