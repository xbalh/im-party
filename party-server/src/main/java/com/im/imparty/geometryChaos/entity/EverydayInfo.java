package com.im.imparty.geometryChaos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EverydayInfo对象", description="")
public class EverydayInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String userName;

    @ApiModelProperty(value = "数量")
    private String everydayNum;

    @ApiModelProperty(value = "累计的天数")
    private String daysCount;

    private Date createTime;
}
