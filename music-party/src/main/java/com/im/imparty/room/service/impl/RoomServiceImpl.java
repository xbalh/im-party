package com.im.imparty.room.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.room.entity.RoomDomain;
import com.im.imparty.room.entity.RoomUserDomain;
import com.im.imparty.room.mapper.RoomMapper;
import com.im.imparty.room.service.RoomService;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public int createRoom(RoomDomain roomInfo) {
        if(StringUtils.isBlank(roomInfo.getRoomName()) || StringUtils.isBlank(roomInfo.getRoomStyle())){
            throw new CustomException("房间名/房间类型不能为空");
        }


        return 0;
    }
}
