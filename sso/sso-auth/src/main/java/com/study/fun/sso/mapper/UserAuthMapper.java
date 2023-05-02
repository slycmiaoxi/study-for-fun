package com.study.fun.sso.mapper;

import com.study.fun.sso.domain.UserProfile;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserAuthMapper {

    @Select("select t1.user_id, t1.user_name, t1.`user_pwd` password, t1.status userStatus from sys_user t1 where t1.user_name = #{username} LIMIT 1")
    UserProfile selectUserAuth(String username);
}
