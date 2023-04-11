package com.im.imparty.geometryChaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.im.imparty.geometryChaos.entity.EverydayInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
public interface EverydayInfoService extends IService<EverydayInfo> {

    /***
     *每日第一次访问获取数据
     * @param requestPage
     * @return
     */
    String getFirstEveryday(EverydayInfo requestPage);
}
