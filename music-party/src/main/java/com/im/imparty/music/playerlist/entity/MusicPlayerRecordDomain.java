package com.im.imparty.music.playerlist.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.im.imparty.common.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 2
 * @since 2023-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("music_player_record")
@ApiModel(value="MusicPlayerRecordDomain对象", description="")
public class MusicPlayerRecordDomain extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String songId;

    private Integer roomId;

    private String crtUsr;

    private Integer sort;

    private String songName;

    private Integer totalTime;

    private String singer;

    private String songQuality;

    private boolean isPlayed;

    private Date crtTim;

}
