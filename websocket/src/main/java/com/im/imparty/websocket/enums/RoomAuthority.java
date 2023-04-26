package com.im.imparty.websocket.enums;

import com.im.imparty.common.enums.BaseIntEnums;

import java.util.List;

public enum RoomAuthority implements BaseIntEnums {
    PLAY(0x1, "播放暂停权限"),
    ADD(0x2, "添加歌曲权限"),
    MODIFY(0x3, "调整歌曲列表");
    private int code;

    private String desc;

    RoomAuthority(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static RoomAuthority[] getAll() {
        return values();
    }
}
