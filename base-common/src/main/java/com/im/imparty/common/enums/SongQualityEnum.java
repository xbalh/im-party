package com.im.imparty.common.enums;

public enum SongQualityEnum{

    STANDARD("standard", "标准"),
    HIGHER("higher", "较高"),
    EXHIGH("exhigh", "极高"),
    LOSSLESS("lossless", "无损");

    private String code;

    private String desc;

    SongQualityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SongQualityEnum check(String songQuality) {
        if (songQuality.contains(LOSSLESS.getCode())) {
            return LOSSLESS;
        } else if (songQuality.contains(EXHIGH.getCode())) {
            return EXHIGH;
        } else if (songQuality.contains(HIGHER.getCode())) {
            return HIGHER;
        } else {
            return STANDARD;
        }
    }
}
