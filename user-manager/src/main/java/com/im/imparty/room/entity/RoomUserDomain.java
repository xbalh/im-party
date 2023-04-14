package com.im.imparty.room.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.im.imparty.common.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 2
 * @since 2023-04-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("room_user")
@ApiModel(value="RoomUserDomain对象", description="")
public class RoomUserDomain extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Integer roomNo;

    private String userName;


}
