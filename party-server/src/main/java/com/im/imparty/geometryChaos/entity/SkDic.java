package com.im.imparty.geometryChaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class SkDic {
    @TableId
    private String id;
    private String skName;
    private String skType;
    private String skDetail;
}
