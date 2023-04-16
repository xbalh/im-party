package com.im.imparty.geometryChaos.controller;


import com.im.imparty.geometryChaos.entity.GachaInfo;
import com.im.imparty.geometryChaos.service.EverydayInfoService;
import com.im.imparty.geometryChaos.service.GachaService;
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
@Api(value = "扭蛋管理", tags = "扭蛋管理")
@Slf4j
@RestController
public class GachaController {

    @Autowired
    private GachaService gachaService;


    @ApiOperation(value = "抽取指定池子")
    @PostMapping("/gacha/getGacha")
    public String getGacha(@RequestBody GachaInfo gachaInfo) {
        String firstEveryday = gachaService.getGacha(gachaInfo);
        return firstEveryday;
    }
}

