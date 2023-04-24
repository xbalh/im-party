package com.im.imparty.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.im.imparty.room.entity.RoomDomain;
import com.im.imparty.room.mapper.RoomMapper;
import com.im.imparty.room.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 2
 * @since 2023-04-14
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomDomain> implements RoomService {

    @Override
    public List<RoomDomain> getRoomList() {
        return getBaseMapper().selectList(Wrappers.emptyWrapper());
    }
}
