package com.im.imparty.geometryChaos.service;

import com.im.imparty.common.exception.CustomException;
import com.im.imparty.geometryChaos.entity.*;

import java.util.List;

public interface PVEMainService {
    /**
     * 新建
     *
     * @param level
     **/
    UserStaticInfo createCharacter(Integer level);

    /**
     * 获取战斗前基础属性
     *
     * @param userName
     **/
    PersonFightInfo getUserFightInfo(String userName) throws CustomException;

}
