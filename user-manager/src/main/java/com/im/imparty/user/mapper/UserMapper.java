package com.im.imparty.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import org.apache.ibatis.annotations.*;

public interface UserMapper extends BaseMapper<UserDomain> {

    @Results({
            @Result(column = "userName", property = "userName"),
            @Result(column = "userName", property = "roleList",
                many = @Many(
                        select = "com.im.imparty.user.mapper.UserRoleRelaMapper.selectByUserName"
                )
            )
    })

    @Select(
            "select u.username userName, u.password password, " +
                    "u.valid_sts validSts, u.salt, u.salt_expires_time saltExpiresTime, " +
                    "u.nick_name nickName " +
                    "from user u " +
                    "where u.username = #{userName}"
    )
    UserInfoDetail getDetail(@Param("userName") String userName);

}
