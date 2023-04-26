package com.im.imparty.common.util;

import com.im.imparty.common.enums.BaseIntEnums;

public class StatusUtils {

    public static boolean checkStatus(int value, BaseIntEnums... authority) {
        int authorityTmp = 0;
        for (BaseIntEnums i : authority) {
            authorityTmp |= i.getCode();
        }
        return (value & authorityTmp) == authorityTmp;
    }

    public static int addAuthority(int value, BaseIntEnums ... authority) {
        int authorityTmp = 0;
        for (BaseIntEnums i : authority) {
            authorityTmp |= i.getCode();
        }
        return value | authorityTmp;
    }

    public static int removeAuthority(int value, BaseIntEnums ... authority) {
        int authorityTmp = 0;
        for (BaseIntEnums i : authority) {
            authorityTmp |= i.getCode();
        }
        return value & (value ^ authorityTmp);
    }
}
