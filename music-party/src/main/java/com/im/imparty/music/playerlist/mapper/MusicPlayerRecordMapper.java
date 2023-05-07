package com.im.imparty.music.playerlist.mapper;

import com.im.imparty.music.playerlist.entity.MusicPlayerRecordDomain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 2
 * @since 2023-05-07
 */
public interface MusicPlayerRecordMapper extends BaseMapper<MusicPlayerRecordDomain> {

    @Select("select max(sort) from music_player_record where room_id = #{roomId}")
    Integer selectMaxSort(@Param("roomId") Integer roomId);

}
