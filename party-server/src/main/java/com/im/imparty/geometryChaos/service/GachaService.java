package com.im.imparty.geometryChaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.im.imparty.geometryChaos.entity.EverydayInfo;
import com.im.imparty.geometryChaos.entity.GachaInfo;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
public interface GachaService extends IService<GachaInfo> {

    /***
     *单抽
     * @param
     * @return
     */
    String getGacha(GachaInfo gachaInfo);
}
