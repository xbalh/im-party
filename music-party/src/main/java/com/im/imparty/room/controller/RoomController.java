package com.im.imparty.room.controller;


import com.im.imparty.common.BaseController;
import com.im.imparty.room.entity.RoomDomain;
import com.im.imparty.room.service.RoomService;
import com.im.imparty.web.vo.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 2
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/room")
public class RoomController extends BaseController {

    @Resource
    private RoomService roomService;

    @GetMapping("/list")
    public BaseResult<List<RoomDomain>> getRoomList() {
        return BaseResult.ok(roomService.getRoomList());
    }

}
