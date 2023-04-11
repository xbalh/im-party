package com.im.imparty.geometryChaos.controller;


import com.im.imparty.geometryChaos.entity.EverydayInfo;
import com.im.imparty.geometryChaos.service.EverydayInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
@Api(value = "签到管理", tags = "签到管理")
@Slf4j
@RestController
public class EverydayInfoController {

    @Autowired
    private EverydayInfoService everydayInfoService;


    @ApiOperation(value = "获取每日第一次信息")
    @PostMapping("/everyday/getFirstEveryday")
    public String getFirstEveryday(@RequestBody EverydayInfo everydayInfo) {
        String firstEveryday = everydayInfoService.getFirstEveryday(everydayInfo);
        return firstEveryday;
    }


}

