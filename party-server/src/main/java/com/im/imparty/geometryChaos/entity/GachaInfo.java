package com.im.imparty.geometryChaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class GachaInfo {
    @TableId
    private Integer id;
    private String gachaName;
    private Integer cost;
    private Integer ssr;
    private Integer sr;
    private Integer r;
    private Integer n;
    private String gachaDetail;
}
