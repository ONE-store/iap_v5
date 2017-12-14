package com.onestore.extern.iap;

/**
 * ONE store In-app Purchase AIDL Interface
 */
interface IInAppPurchaseService {

    /**
     * 결제 지원 정보를 확인한다.
     */
     int isBillingSupported(int apiVersion, String packageName);

    /**
     * 로그인 Intent 정보를 가져온다.
     */
     Bundle getLoginIntent(int apiVersion, String packageName);

    /**
     * 상품 정보를 가져온다.
     */
     Bundle getProductDetails(int apiVersion, String packageName, String productType, in Bundle productsBundle);

    /**
     * 구매 Intent 정보를 가져온다.
     */
     Bundle getPurchaseIntent(int apiVersion, String packageName, String productId, String productName, String productType, String developerPayload);

     /**
     * 구매 Intent 정보를 가져온다. (extraParams를 위한 확장 API)
     */
     Bundle getPurchaseIntentExtraParams(int apiVersion, String packageName, String productId, String productName, String productType, String developerPayload, in Bundle extraParams);

    /**
     * 아직 소모되지 않은 관리형상품(inapp)정보와 구매 중인 월정액상품(auto)정보를 받아온다.
     */
     Bundle getPurchases(int apiVersion, String packageName, String productType, String continuationKey);

    /**
     * 관리형상품(inapp)을 소모 한다.
     */
     Bundle consumePurchase(int apiVersion, String packageName, String purchaseId);

    /**
     * 월정액상품(auto)의 자동결제 해지예약 및 해지예약 취소를 요청한다.
     */
     Bundle manageRecurringProduct(int apiVersion, String packageName, String action, String purchaseId);
}