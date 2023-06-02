package com.im.imparty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.user.mapper.UserRoleRelaMapper;
import com.im.imparty.user.entity.UserRoleRelaDomain;
import com.im.imparty.user.service.UserRoleRela;
import org.springframework.stereotype.Service;

@Service
public class UserRoleRelaImpl extends ServiceImpl<UserRoleRelaMapper, UserRoleRelaDomain> implements UserRoleRela {
}
