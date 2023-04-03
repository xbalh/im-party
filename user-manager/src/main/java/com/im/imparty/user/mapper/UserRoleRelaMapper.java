package com.im.imparty.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.im.imparty.user.dto.RoleInfo;
import com.im.imparty.user.entity.UserRoleRelaDomain;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleRelaMapper extends BaseMapper<UserRoleRelaDomain> {

    @Select(
            "select username userName, role_code roleCode from user_role_rela where username = #{userName}"
    )
    List<RoleInfo> selectByUserName(String userName);

}
