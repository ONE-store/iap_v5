package com.onestore.iap.sample.util;

/**
 * Lucky ONE - 게임 동작에 필요한 상수 값
 */

public class AppConstant {
    private AppConstant() {
        throw new IllegalAccessError("Utility class");
    }

    /*
     * Custom pid : 허용 가능한 문자 셋 (1-9, a-z, .(점), _(밑줄))
     */

    // 소모성 상품코드 - 5000원권 아이템
    public static final String PRODUCT_INAPP_5000 = "p5000";

    // 소모성 상품코드 - 10,000원권 아이템
    public static final String PRODUCT_INAPP_10000 = "p10000";

    // 소모성 상품코드 - 50,000원권 아이템
    public static final String PRODUCT_INAPP_50000 = "p50000";

    // 월정액 상품코드 - 100,000원권 아이템
    public static final String PRODUCT_AUTO_100000 = "a100000";


    // Lucky ONE Shared Preference Key
    public static final String KEY_MODE_MONTHLY = "MODE_MONTHLY";
    public static final String KEY_TOTAL_COIN = "TOTAL_COIN";
    public static final String KEY_PAYLOAD = "PAYLOAD";
    public static final String KEY_IS_FIRST = "IS_FIRST";

}
