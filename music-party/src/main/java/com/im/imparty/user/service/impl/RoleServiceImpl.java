package com.im.imparty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.user.entity.RoleDomain;
import com.im.imparty.user.mapper.RoleMapper;
import com.im.imparty.user.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDomain> implements RoleService {
}
