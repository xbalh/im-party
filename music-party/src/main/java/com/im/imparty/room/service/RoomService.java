package com.im.imparty.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.im.imparty.room.entity.RoomDomain;
import com.im.imparty.room.entity.RoomUserDomain;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 2
 * @since 2023-04-14
 */
public interface RoomService extends IService<RoomDomain> {

    List<RoomDomain> getRoomList();

    int createRoom(RoomDomain roomInfo);

}
