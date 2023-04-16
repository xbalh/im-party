package com.im.imparty.common.dic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LevelDic {
    private static final BigDecimal BASE_EXPERIENCE = BigDecimal.TEN;
    private static final BigDecimal EXPERIENCE_RATE = new BigDecimal("1.01");

    public static BigDecimal calculateExperience(int level) {
        BigDecimal expx = BASE_EXPERIENCE;
        for (int i = 2; i <= level; i++) {
            expx = expx.multiply(EXPERIENCE_RATE).setScale(0, RoundingMode.HALF_UP).add(BigDecimal.ONE);
        }
        return expx;
    }
}