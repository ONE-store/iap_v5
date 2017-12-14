package com.onestore.iap.sample.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.onestore.iap.api.IapEnum;
import com.onestore.iap.api.IapResult;
import com.onestore.iap.api.ProductDetail;
import com.onestore.iap.api.PurchaseClient;
import com.onestore.iap.api.PurchaseData;
import com.onestore.iap.sample.R;
import com.onestore.iap.sample.security.AppSecurity;
import com.onestore.iap.sample.util.AppConstant;
import com.onestore.iap.sample.util.LuckyUtils;
import com.onestore.iap.sample.view.BuyCoinsPopup;
import com.onestore.iap.sample.view.LuckyMainView;
import com.onestore.iap.sample.view.SettingPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Lucky ONE
 * <p>
 * 원스토어 In-app purchasing v17을 이용한 게임
 * <p>
 * TODO: Lucky ONE 샘플 앱에서는 개발사에서 원스토어 인앱결제를 이용하기 위한 일반적인 IAP v17 API 구매 시나리오를 나열하고 있습니다. 샘플 구동 전에 해당 가이드를 꼼꼼히 읽어보시기 바랍니다.
 * <p>
 * Lucky ONE 샘플 앱은 많은 사람들이 사랑하는 로또의 번호 추천 시스템과 비슷하게 개발되었습니다. 사용자는 번호를 생성하여 추첨을 하기 위해서 사용자는 코인을 구매하여야 하며, 구매된 코인을 이용하여 게임을 이용할 수 있습니다.
 * <p>
 * 메인화면에서는 보유코인 수와 사용자의 추천 랜덤 번호, 그리고 실제 추첨된 5개의 번호를 나열하고 있습니다.
 * 3개의 번호 일치시 5개의 코인을 지급하며, 4개 일치 시 30개 코인을 지급, 5개일때는 100개의 코인, 6개일때는 300개의 코인을 지급합니다.
 * 또한, 월정액 상품을 제공하여 보유한 코인 갯수와 무관하게 계속해서 번호를 생성할 수 있는 상품이 있습니다.
 * <p>
 * 사용자는 구매 버튼을 선택 할 경우 상품을 선택할 수 있는 창이 뜨게되며, 사용자의 선택에 따라 5, 10, 60coins(관리형상품)과 월정액상품 구매를 할 수 있습니다.
 * 관리형상품의 경우 구매를 완료하게 될 경우 상품소비를 통하여 해당 구매상품 소비과정을 진행해야합니다. 또한 월정액상품은 구매 이후 각 개발사에서 자체적으로 구매 상태를 관리하여야 합니다.
 * 샘플에서 월정액상품의 구매 상태를 관리하기 위하여 애플리케이션 프리퍼런스에 해당 정보를 저장하고있으며, 이후에 구매 정보가 필요할 시점에 사용하고 있습니다.
 * <p>
 * Lucky ONE 앱에서는 앱 구동시점에 구매내역조회 API를 이용하여 관리형상품(inapp)/월정액상품(auto)에 대하여 구매내역 정보를 받아오고 있으며,
 * 관리형상품(inapp) 구매내역을 받아올 경우 상품소비가 진행되지 않는 상품이므로 상품소비과정을 진행합니다.
 * 또한 월정액상품(auto) 구매내역을 받아올 경우 메인화면 하단의 "번호 생성하기" 버튼을 무한으로 플레이 할 수 있도록 변경하고 있습니다.
 * <p>
 * 월정액상품(auto)의 경우 구매내역의 recurringState 정보에 따라 설정화면에서 월정액상품(auto) 해지예약과 해지예약 취소 버튼을 노출하고 있습니다.
 * <p>
 * <p>
 * - Lucky ONE 샘플앱 구동 전 준비 사항 -
 * 1. 샘플앱의 패키지 이름을 변경합니다.
 * 3. AppSecurity 클래스의 getPublicKey()메서드를 이용하기 위하여 jin/public_keys.c 코드 상의 변경된 패키지 이름에 맞게 Native 메서드 명을 변경해줍니다.
 * 2. 원스토어 개발자센터에 애플리케이션을 등록 후 base64 public key 값을 jin/public_keys.c 코드 내에 붙여넣습니다.
 * 4. 원스토어 개발자센터에 상품을 등록합니다. 샘플앱의 경우 상품 정보는 AppConstant 클래스에 있습니다.
 * 5. 애플리케이션 위변조 체크를 위해서 개발자 센터에 등록하는 앱은 사이닝이 된 APK여야 합니다. (앱 사이닝 없이 테스트를 위해서는 Sandbox 계정을 등록하여 확인하시면 됩니다.)
 */

public class LuckyActivity extends Activity {
    private final String TAG = getClass().getSimpleName();
    private static final int IAP_API_VERSION = 5;
    private static final int COIN_PER_GAME = 5;

    // 구매 진행시 Activity 결과 받기 위해서 사용되는 Request 코드로서 개발자 임의로 수정가능함.
    private static final int LOGIN_REQUEST_CODE = 1001;
    private static final int PURCHASE_REQUEST_CODE = 2001;

    private LuckyMainView mLuckyView;
    private PurchaseClient mPurchaseClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        initLayout();
        initHelper();
    }

    private void initLayout() {
        setContentView(R.layout.activity_lucky);
        mLuckyView = (LuckyMainView) findViewById(R.id.view_lucky_main);
        mLuckyView.setUserActionListener(mMainCallback);

        // 첫 시작시 20코인 지급
        if (getIsFirst()) {
            updateCoin(20);
            updateIsFirst();
        }

        // 게임 코인 UI업데이트
        mLuckyView.updateGameCoin(getCurrentCoin());
        // 게임 버튼 UI업데이트
        mLuckyView.updateGenerateButton(isMonthlyItemAvailable());
    }

    /*
     * TODO: PurchaseClient 초기화시에 필요한 public key는 원스토어 개발자센터에서 받아온 키를 이용하여아합니다.
     *
     * PurchaseCilent 초기화를 진행하며, 원스토어 서비스로 인앤결제를 위한 서비스 바인딩을 요청합니다.
     */
    private void initHelper() {
        // PurchaseClient 초기화 - context 와 Signature 체크를 위한 public key 를 파라미터로 넘겨줍니다.
        mPurchaseClient = new PurchaseClient(this, AppSecurity.getPublicKey());

        // 원스토어 서비스로 인앱결제를 위한 서비스 바인딩을 요청합니다.
        mPurchaseClient.connect(mServiceConnectionListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        // 앱 종료시 PurchaseClient를 이용하여 서비스를 terminate 시킵니다.
        mPurchaseClient.terminate();
    }

    /*
     * UI - 세팅화면 콜백
     */
    private SettingPopup.UserCallback mSettingCallback = new SettingPopup.UserCallback() {
        @Override
        public void manageRecurringProduct() {

            PurchaseData purchaseData = getMonthlyItem();

            int recurringState = purchaseData.getRecurringState();
            String action;

            // SharedPreference 에 저장해놓은 월정액의 RecurringState 값에 따라 월정액 해지예약 또는 해지예약 취소 동작을 수행합니다.
            if (IapEnum.RecurringState.AUTO_PAYMENT.getType() == recurringState) {
                // 월정액 해지예약
                action = IapEnum.RecurringAction.CANCEL.getType();

            } else if (IapEnum.RecurringState.CANCEL_RESERVATION.getType() == recurringState) {
                // 월정액 해지예약 취소
                action = IapEnum.RecurringAction.REACTIVATE.getType();

            } else {
                action = "";
            }

            // 월정액 해지예약 또는 해지예약 취소 수행
            manageRecurringAuto(purchaseData, action);
        }
    };

    /*
     * UI - 코인 구매 선택화면 콜백
     */
    private BuyCoinsPopup.UserCallback mBuyCoinsCallback = new BuyCoinsPopup.UserCallback() {
        @Override
        public void buyCoin(String item) {
            if (isMonthlyItemAvailable()) {
                // 월정액상품(auto)을 구매 중일 경우 "inapp" 상품 구매하지 못합니다.
                alert(getString(R.string.desc_setting_auto_purchased_ko) + "\n\n" + getString(R.string.desc_setting_auto_purchased_en));
            } else {
                // 구매요청
                buyProduct(item, getItemType(item));
            }
        }
    };

    /*
     * UI - 메인화면 UI콜백
     */
    private LuckyMainView.UserCallback mMainCallback = new LuckyMainView.UserCallback() {
        @Override
        public void goSetting() {

            IapEnum.RecurringState recurringState;

            // 설정화면 진입 전 월정액 UI 업데이트를 위하여,
            // 현재 저장된 월정액상품(auto) recurringState 정보를 설정화면으로 넘겨줍니다.
            if (isMonthlyItemAvailable()) {
                if (IapEnum.RecurringState.AUTO_PAYMENT.getType() == getMonthlyItem().getRecurringState()) {
                    // 월정액 상태 - 자동결제중
                    recurringState = IapEnum.RecurringState.AUTO_PAYMENT;
                } else {
                    // 월정액 상태 - 해지예약중
                    recurringState = IapEnum.RecurringState.CANCEL_RESERVATION;
                }

            } else {
                recurringState = IapEnum.RecurringState.NON_AUTO_PRODUCT;
            }

            SettingPopup settingPopup = new SettingPopup(LuckyActivity.this, mSettingCallback, isMonthlyItemAvailable(), recurringState);
            settingPopup.show();
        }

        @Override
        public void goBuy() {

            /*
             * TODO: 개발사에서는 게임 내 상품정보와 개발자 센터에 등록된 상품이 일치하는지 확인이 필요합니다.
             *
             * 코인 선택화면 진입 전 원스토어 개발자 센터에 등록된 상품 정보를 받아옵니다.
             */

            loadAllProducts();

            BuyCoinsPopup buyCoinsPopup = new BuyCoinsPopup(LuckyActivity.this, mBuyCoinsCallback);
            buyCoinsPopup.show();
        }

        @Override
        public void generateNumber() {
            // Lucky 번호 생성하기
            playGame();
        }
    };


    // 구매요청
    private void buyProduct(final String productId, final IapEnum.ProductType productType) {
        Log.d(TAG, "buyProduct() - productId:" + productId + " productType: " + productType.getType());

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        /*
         * TODO: AppSecurity.generatePayload() 는 예제일 뿐, 각 개발사의 규칙에 맞는 payload를 생성하여야 한다.
         *
         * 구매요청을 위한 Developer payload를 생성합니다.
         * Developer Payload 는 상품의 구매 요청 시에 개발자가 임의로 지정 할 수 있는 문자열입니다.
         * 이 Payload 값은 결제 완료 이후에 응답 값에 다시 전달 받게 되며 결제 요청 시의 값과 차이가 있다면 구매 요청에 변조가 있다고 판단 하면 됩니다.
         * Payload 검증을 통해 Freedom 과 같은 변조 된 요청을 차단 할 수 있으며, Payload 의 발급 및 검증 프로세스를 자체 서버를 통해 이루어 지도록합니다.
         */
        String devPayload = AppSecurity.generatePayload();

        // 구매 후 dev payload를 검증하기 위하여 프리퍼런스에 임시로 저장합니다.
        savePayloadString(devPayload);
        showProgress(this);

        /*
         * PurchaseClient의 launchPurchaseFlowAsync API를 이용하여 구매요청을 진행합니다.
         * 상품명을 공백("")으로 요청할 경우 개발자센터에 등록된 상품명을 결제화면에 노출됩니다. 구매시 지정할 경우 해당 문자열이 결제화면에 노출됩니다.
         */
        if (mPurchaseClient.launchPurchaseFlowAsync(IAP_API_VERSION, this, PURCHASE_REQUEST_CODE, productId, "",
                productType.getType(), devPayload, "", false, mPurchaseFlowListener) == false) {
            // listener is null
        }
    }


    /*
     * 에러 코드 중 로긴이 필요로 할 경우 launchLoginFlowAsync API를 이용하여 명시적 로그인을 수행합니다.
     * launchLoginFlowAsync API를 호출할 경우 UI적으로 원스토어 로그인화면이 뜰 수 있습니다.
     * 개발사에서는 로그인 성공 시 파라미터로 넘겨준 Activity의 onActivityResult에서 Intent값을 전달 받아서,
     * PurchaseClient의 handleLoginResult() API를 이용하여 응답값을 파싱합니다.
     */
    private void loadLoginFlow() {
        Log.d(TAG, "loadLoginFlow()");

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        alertDecision("원스토어 계정 정보를 확인 할 수 없습니다. 로그인 하시겠습니까?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPurchaseClient.launchLoginFlowAsync(IAP_API_VERSION, LuckyActivity.this, LOGIN_REQUEST_CODE, mLoginFlowListener) == false) {
                    // listener is null
                }
            }
        });
    }

    /*
     * 앱구동시 isBillingSupportedAsync API를 이용하여 인앱결제를 진행할 수 있는 상태인지 확인합니다.
     * 이후 구매내역조회 API를 이용하여 소비되지 않는 소모성상품(inapp)과 자동결제중인 월정액상품(auto) 목록을 받아옵니다.
     */
    private void checkBillingSupportedAndLoadPurchases() {
        Log.d(TAG, "checkBillingSupportedAndLoadPurchases()");

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        showProgress(this);
        mPurchaseClient.isBillingSupportedAsync(IAP_API_VERSION, mBillingSupportedListener);
    }


    // 구매내역조회 요청
    private void loadPurchases() {
        Log.d(TAG, "loadPurchases()");

        loadPurchase(IapEnum.ProductType.IN_APP);
        loadPurchase(IapEnum.ProductType.AUTO);
    }


    /**
     * TODO: 개발사에서는 소비되지 않는 관리형상품(inapp)에 대해, 애플리케이션의 적절한 life cycle 에 맞춰 구매내역조회를 진행 후 소비를 진행해야합니다.
     * <p>
     * 구매내역조회 API를 이용하여 소비되지 않는 관리형상품(inapp)과 자동결제중인 월정액상품(auto) 목록을 받아옵니다.
     * 관리형상품(inapp)의 경우 소비를 하지 않을 경우 재구매요청을 하여도 구매가 되지 않습니다. 꼭, 소비 과정을 통하여 소모성상품 소비를 진행하여야합니다.
     * 월정액상품(auto)의 경우 구매내역조회 시 recurringState 정보를 통하여 현재상태정보를 확인할 수 있습니다. -> recurringState 0(자동 결제중), 1(해지 예약중)
     * manageRecurringProduct API를 통해 해지예약요청을 할 경우 recurringState는 0 -> 1로 변경됩니다. 해지예약 취소요청을 할경우 recurringState는 1 -> 0로 변경됩니다.
     * <p>
     * 월정액상품(auto)을 11월 10일에 구매를 할 경우 구매내역조회에서 월정액상품의 recurringState는 0(자동 결제중)으로 내려옵니다.
     * 월정액상품은 매달 구매일(12월 10일)에 자동결제가 발생하므로 11월 10일 ~ 12월 9일까지 현재 상태를 유지합니다.
     * 11월 15일에 월정액상태변경API를 이용하여 해지예약(cancel)을 진행할 경우, 12월 9일까지 월정액상품 상태(recurringState)는 1(해지 예약중)이 됩니다.
     * 12월 9일 이전에 월정액상태변경API를 이용하여 해지예약 취소(reactivate)를 진행할 경우, 해당 상품의 상태(recurringState)는 0(자동 결제중)이 됩니다.
     */
    private void loadPurchase(final IapEnum.ProductType productType) {
        Log.i(TAG, "loadPurchase() :: productType - " + productType.getType());

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        showProgress(this);

        mPurchaseClient.queryPurchasesAsync(IAP_API_VERSION, productType.getType(), mQueryPurchaseListener);
    }


    // 구매내역조회에서 받아온 관리형상품(inapp)의 경우 Signature 검증을 진행하고, 성공할 경우 상품소비를 진행합니다.
    private void onLoadPurchaseInApp(List<PurchaseData> purchaseDataList) {
        Log.i(TAG, "onLoadPurchaseInApp() :: purchaseDataList - " + purchaseDataList.toString());

        for (PurchaseData purchase : purchaseDataList) {
            boolean result = AppSecurity.verifyPurchase(purchase.getPurchaseData(), purchase.getSignature());

            if (result) {
                consumeItem(purchase);
            }
        }
    }

    // 구매내역조회에서 받아온 월정액상품(auto)의 경우 Signature 검증을 진행하고, 성공할 경우 게임 UI 시나리오를 위해 상품정보를 저장해놓습니다.
    private void onLoadPurchaseAuto(List<PurchaseData> purchaseDataList) {
        Log.i(TAG, "onLoadPurchaseAuto() :: purchaseDataList - " + purchaseDataList.toString());

        if (purchaseDataList.isEmpty()) {
            saveMonthlyItem(null);
        }

        for (PurchaseData purchase : purchaseDataList) {
            boolean result = AppSecurity.verifyPurchase(purchase.getPurchaseData(), purchase.getSignature());

            if (result) {
                // 샘플앱의 경우 한가지의 월정액상품만 제공하고 있습니다.
                saveMonthlyItem(purchase);
            }
        }
    }

    // 개발자센터에 등록된 상품정보를 조회합니다. 개발사에서는 상품정보 조회를 하고자 하는 상품ID를 String ArrayList로 전달합니다.
    private void loadProducts(IapEnum.ProductType productType, final ArrayList<String> products) {
        Log.d(TAG, "loadProducts");

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        showProgress(this);

        mPurchaseClient.queryProductsAsync(IAP_API_VERSION, products, productType.getType(), mQueryProductsListener);
    }

    // 관리형상품(inapp)의 구매완료 이후 또는 구매내역조회 이후 소비되지 않는 관리형상품에 대해서 소비를 진행합니다.
    private void consumeItem(final PurchaseData purchaseData) {
        Log.e(TAG, "consumeItem() :: getProductId - " + purchaseData.getProductId() + " getPurchaseId -" + purchaseData.getPurchaseId());

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        mPurchaseClient.consumeAsync(IAP_API_VERSION, purchaseData, mConsumeListener);
    }

    // 월정액상품(auto)의 상태변경(해지예약 / 해지예약 취소)를 진행합니다.
    private void manageRecurringAuto(final PurchaseData purchaseData, final String action) {
        Log.d(TAG, "manageRecurringAuto() :: action - " + action + " purchaseId - " + purchaseData.getPurchaseId());

        if (mPurchaseClient == null) {
            Log.d(TAG, "PurchaseClient is not initialized");
            return;
        }

        showProgress(this);

        mPurchaseClient.manageRecurringProductAsync(IAP_API_VERSION, purchaseData, action, mManageRecurringProductListener);
    }


    /*
     * PurchaseClient의 connect API 콜백 리스너
     * 바인딩 성공/실패 및 원스토어 서비스 업데이트가 필요한지에 대한 응답을 넘겨줍니다.
     */
    PurchaseClient.ServiceConnectionListener mServiceConnectionListener = new PurchaseClient.ServiceConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(TAG, "Service connected");
            checkBillingSupportedAndLoadPurchases();
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "Service disconnected");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "connect onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            updateOrInstallOneStoreService();
        }
    };

    /*
     * PurchaseClient의 isBillingSupportedAsync (지원여부조회) API 콜백 리스너
     */
    PurchaseClient.BillingSupportedListener mBillingSupportedListener = new PurchaseClient.BillingSupportedListener() {

        @Override
        public void onSuccess() {
            Log.d(TAG, "isBillingSupportedAsync onSuccess");
            hideProgress();

            // isBillingSupportedAsync 호출 성공시에,
            // 구매내역정보를 호출을 진행하여 관리형상품 및 월정액상품 구매내역에 대해서 받아옵니다.
            loadPurchases();
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "isBillingSupportedAsync onError, " + result.toString());
            hideProgress();

            // RESULT_NEED_LOGIN 에러시에 개발사의 애플리키에션 life cycle에 맞춰 명시적으로 원스토어 로그인을 호출합니다.
            if (IapResult.RESULT_NEED_LOGIN == result) {
                loadLoginFlow();
            }
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "isBillingSupportedAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");
            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "isBillingSupportedAsync onError, 비정상 앱에서 결제가 요청되었습니다");
            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "isBillingSupportedAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            hideProgress();
            updateOrInstallOneStoreService();
        }
    };

    /*
     * PurchaseClient의 queryProductsAsync API (상품정보조회) 콜백 리스너
     */
    PurchaseClient.QueryProductsListener mQueryProductsListener = new PurchaseClient.QueryProductsListener() {
        @Override
        public void onSuccess(List<ProductDetail> productDetails) {
            Log.d(TAG, "queryProductsAsync onSuccess, " + productDetails.toString());
            hideProgress();
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "queryProductsAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");
            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "queryProductsAsync onError, 비정상 앱에서 결제가 요청되었습니다");
            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "queryProductsAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            hideProgress();
            updateOrInstallOneStoreService();
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "queryProductsAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }
    };

    /*
     * PurchaseClient의 queryPurchasesAsync API (구매내역조회) 콜백 리스너
     */
    PurchaseClient.QueryPurchaseListener mQueryPurchaseListener = new PurchaseClient.QueryPurchaseListener() {
        @Override
        public void onSuccess(List<PurchaseData> purchaseDataList, String productType) {
            Log.d(TAG, "queryPurchasesAsync onSuccess, " + purchaseDataList.toString());
            hideProgress();

            if (IapEnum.ProductType.IN_APP.getType().equalsIgnoreCase(productType)) {
                onLoadPurchaseInApp(purchaseDataList);

            } else if (IapEnum.ProductType.AUTO.getType().equalsIgnoreCase(productType)) {
                onLoadPurchaseAuto(purchaseDataList);
            }
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "queryPurchasesAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");
            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "queryPurchasesAsync onError, 비정상 앱에서 결제가 요청되었습니다");
            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "queryPurchasesAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            hideProgress();
            updateOrInstallOneStoreService();
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "queryPurchasesAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }
    };


    /*
     * PurchaseClient의 consumeAsync API (상품소비) 콜백 리스너
     */
    PurchaseClient.ConsumeListener mConsumeListener = new PurchaseClient.ConsumeListener() {
        @Override
        public void onSuccess(PurchaseData purchaseData) {
            Log.d(TAG, "consumeAsync onSuccess, " + purchaseData.toString());
            hideProgress();
            updateCoinsPurchased(purchaseData.getProductId());
            String text = String.format(getString(R.string.msg_purchase_success_inapp), getPurchasedCoins(purchaseData.getProductId()));
            alert(text, true);
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "consumeAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");
            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "consumeAsync onError, 비정상 앱에서 결제가 요청되었습니다");
            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "consumeAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            hideProgress();
            updateOrInstallOneStoreService();
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "consumeAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }
    };

    /*
     * PurchaseClient의 manageRecurringProductAsync API (월정액상품 상태변경) 콜백 리스너
     */
    PurchaseClient.ManageRecurringProductListener mManageRecurringProductListener = new PurchaseClient.ManageRecurringProductListener() {
        @Override
        public void onSuccess(PurchaseData purchaseData, String manageAction) {
            Log.d(TAG, "manageRecurringProductAsync onSuccess, " + manageAction + " " + purchaseData.toString());
            hideProgress();

            if (IapEnum.RecurringAction.CANCEL.getType().equalsIgnoreCase(manageAction)) {
                alert(getString(R.string.msg_setting_cancel_auto_complete));
            } else {
                alert(getString(R.string.msg_setting_resubscribe_auto_complete));
            }

            loadPurchase(IapEnum.ProductType.AUTO);
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "manageRecurringProductAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");

            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "manageRecurringProductAsync onError, 비정상 앱에서 결제가 요청되었습니다");

            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "manageRecurringProductAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");

            hideProgress();
            updateOrInstallOneStoreService();
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "manageRecurringProductAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }
    };

    /*
     * PurchaseClient의 launchPurchaseFlowAsync API (구매) 콜백 리스너
     */
    PurchaseClient.PurchaseFlowListener mPurchaseFlowListener = new PurchaseClient.PurchaseFlowListener() {
        @Override
        public void onSuccess(PurchaseData purchaseData) {
            Log.d(TAG, "launchPurchaseFlowAsync onSuccess, " + purchaseData.toString());
            hideProgress();

            // 구매완료 후 developer payload 검증을 수해한다.
            if (!isValidPayload(purchaseData.getDeveloperPayload())) {
                Log.d(TAG, "onSuccess() :: payload is not valid.");
                alert(getString(R.string.msg_alert_dev_payload_invalid));
                return;
            }

            // 구매완료 후 signature 검증을 수행한다.
            boolean validPurchase = AppSecurity.verifyPurchase(purchaseData.getPurchaseData(), purchaseData.getSignature());
            Log.d(TAG, "onSuccess() :: verifyPurchase " + validPurchase);

            if (validPurchase) {
                if (AppConstant.PRODUCT_AUTO_100000.equals(purchaseData.getProductId())) {
                    // 월정액상품이면 소비를 수행하지 않는다.
                    alert(getString(R.string.msg_purchase_success_auto), true);
                    saveMonthlyItem(purchaseData);

                } else {
                    // 관리형상품(inapp)은 구매 완료 후 소비를 수행한다.
                    consumeItem(purchaseData);
                }
            } else {
                alert(getString(R.string.msg_alert_signature_invalid));
            }
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "launchPurchaseFlowAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "launchPurchaseFlowAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");
            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "launchPurchaseFlowAsync onError, 비정상 앱에서 결제가 요청되었습니다");
            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "launchPurchaseFlowAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");
            hideProgress();
            updateOrInstallOneStoreService();
        }
    };

    /*
     * PurchaseClient의 launchLoginFlowAsync API (로그인) 콜백 리스너
     */
    PurchaseClient.LoginFlowListener mLoginFlowListener = new PurchaseClient.LoginFlowListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "launchLoginFlowAsync onSuccess");
            hideProgress();
            // 개발사에서는 로그인 성공시에 대한 이후 시나리오를 지정하여야 합니다.
        }

        @Override
        public void onError(IapResult result) {
            Log.e(TAG, "launchLoginFlowAsync onError, " + result.toString());
            hideProgress();
            alert(result.getDescription());
        }

        @Override
        public void onErrorRemoteException() {
            Log.e(TAG, "launchLoginFlowAsync onError, 원스토어 서비스와 연결을 할 수 없습니다");

            hideProgress();
            alert("원스토어 서비스와 연결을 할 수 없습니다");
        }

        @Override
        public void onErrorSecurityException() {
            Log.e(TAG, "launchLoginFlowAsync onError, 비정상 앱에서 결제가 요청되었습니다");

            hideProgress();
            alert("비정상 앱에서 결제가 요청되었습니다");
        }

        @Override
        public void onErrorNeedUpdateException() {
            Log.e(TAG, "launchLoginFlowAsync onError, 원스토어 서비스앱의 업데이트가 필요합니다");

            hideProgress();
            updateOrInstallOneStoreService();
        }

    };


    /*
     * 게임 내 상품의 상품정보조회를 진행합니다.
     * 개발사에서는 상품정보조회를 진행할 상품ID를 인자로 넘겨줘야합니다.
     */
    private void loadAllProducts() {
        ArrayList<String> inapp = new ArrayList<>();
        inapp.add(AppConstant.PRODUCT_INAPP_5000);
        inapp.add(AppConstant.PRODUCT_INAPP_10000);
        inapp.add(AppConstant.PRODUCT_INAPP_50000);
        loadProducts(IapEnum.ProductType.IN_APP, inapp);

        ArrayList<String> auto = new ArrayList<>();
        auto.add(AppConstant.PRODUCT_AUTO_100000);
        loadProducts(IapEnum.ProductType.AUTO, auto);
    }

    private IapEnum.ProductType getItemType(String itemName) {
        if (itemName.equals(AppConstant.PRODUCT_INAPP_5000)
                || itemName.equals(AppConstant.PRODUCT_INAPP_10000)
                || itemName.equals(AppConstant.PRODUCT_INAPP_50000)) {
            return IapEnum.ProductType.IN_APP;

        } else if (itemName.equals(AppConstant.PRODUCT_AUTO_100000)) {
            return IapEnum.ProductType.AUTO;
        }
        return null;
    }

    private void playGame() {
        if (isMonthlyItemAvailable() == false && getCurrentCoin() < COIN_PER_GAME) {

            alertDecision(getString(R.string.msg_alert_no_coin), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BuyCoinsPopup buyCoinsPopup = new BuyCoinsPopup(LuckyActivity.this, mBuyCoinsCallback);
                    buyCoinsPopup.show();
                }
            });
            return;
        }

        if (isMonthlyItemAvailable() == false) {
            payCoinPerGame();
        }

        List<Integer> luckyBall = LuckyUtils.getMyNumbers();
        List<List<Integer>> myBalls = LuckyUtils.getSuggestNumbers();

        mLuckyView.clearResult();

        for (List<Integer> myBall : myBalls) {
            mLuckyView.setData(luckyBall, myBall);
        }

        int earningCoin = LuckyUtils.getWonCoin(luckyBall, myBalls);

        if (earningCoin > 0) {
            updateCoin(earningCoin);
            String param = getResources().getString(R.string.msg_earning_coins, earningCoin);
            alert(param, true);
        }
    }

    private boolean isValidPayload(String payload) {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String savedPayload = sp.getString(AppConstant.KEY_PAYLOAD, "");

        Log.d(TAG, "isValidPayload saved payload ::" + savedPayload);
        Log.d(TAG, "isValidPayload server payload ::" + payload);

        return savedPayload.equals(payload);
    }

    private void updateGameCoinView(final int coin) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLuckyView.updateGameCoin(coin);
            }
        });
    }

    private void updateButtonView(final boolean isAuto) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLuckyView.updateGenerateButton(isAuto);
            }
        });
    }

    private void updateIsFirst() {
        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        spe.putBoolean(AppConstant.KEY_IS_FIRST, false);
        spe.commit();
    }

    private boolean getIsFirst() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        Log.d(TAG, "getIsFirst - " + sp.getBoolean(AppConstant.KEY_IS_FIRST, true));
        return sp.getBoolean(AppConstant.KEY_IS_FIRST, true);
    }

    private void savePayloadString(String payload) {
        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        spe.putString(AppConstant.KEY_PAYLOAD, payload);
        spe.commit();
    }

    private int getCurrentCoin() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        int coin = sp.getInt(AppConstant.KEY_TOTAL_COIN, 0);
        Log.d(TAG, "getCurrentCoin - " + coin);

        return coin;
    }

    public void payCoinPerGame() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();

        int savedCoins = sp.getInt(AppConstant.KEY_TOTAL_COIN, 0);
        savedCoins -= 5;

        spe.putInt(AppConstant.KEY_TOTAL_COIN, savedCoins);
        spe.commit();

        updateGameCoinView(savedCoins);
    }

    public void updateCoin(int coin) {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();

        int savedCoins = sp.getInt(AppConstant.KEY_TOTAL_COIN, 0);
        spe.putInt(AppConstant.KEY_TOTAL_COIN, coin + savedCoins);
        spe.commit();

        updateGameCoinView(coin + savedCoins);
    }

    private void saveMonthlyItem(PurchaseData purchaseData) {
        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();

        if (purchaseData == null) {
            spe.remove(AppConstant.PRODUCT_AUTO_100000);
            spe.putBoolean(AppConstant.KEY_MODE_MONTHLY, false);
            updateButtonView(false);

        } else {
            Gson gson = new Gson();
            String json = gson.toJson(purchaseData);

            spe.putString(AppConstant.PRODUCT_AUTO_100000, json);
            spe.putBoolean(AppConstant.KEY_MODE_MONTHLY, true);
            updateButtonView(true);
        }

        spe.commit();
    }

    private PurchaseData getMonthlyItem() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString(AppConstant.PRODUCT_AUTO_100000, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        PurchaseData purchaseData = gson.fromJson(json, PurchaseData.class);
        return purchaseData;
    }

    public boolean isMonthlyItemAvailable() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        return sp.getBoolean(AppConstant.KEY_MODE_MONTHLY, false);
    }

    private void updateCoinsPurchased(String item) {
        int coins = getPurchasedCoins(item);
        updateCoin(coins);
    }

    private int getPurchasedCoins(String item) {
        switch (item) {
            case AppConstant.PRODUCT_INAPP_5000:
                return 5;
            case AppConstant.PRODUCT_INAPP_10000:
                return 10;
            case AppConstant.PRODUCT_INAPP_50000:
                return 60;
            default:
                return 0;
        }
    }

    public void showProgress(final Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    return;
                } else {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Server connection...");
                    progressDialog.show();
                }
            }
        });

    }

    private void updateOrInstallOneStoreService() {
        PurchaseClient.launchUpdateOrInstallFlow(this);
    }

    public void alert(final String message) {
        alert(message, false);
    }

    public void alert(final String message, final boolean isHtml) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder bld = new AlertDialog.Builder(LuckyActivity.this);
                bld.setMessage(isHtml ? Html.fromHtml(message) : message)
                        .setPositiveButton(R.string.btn_ok, null)
                        .create()
                        .show();
            }
        });
    }

    public void alertDecision(final String message, final DialogInterface.OnClickListener posListener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder bld = new AlertDialog.Builder(LuckyActivity.this);
                bld.setMessage(message)
                        .setPositiveButton(R.string.btn_yes, posListener)
                        .setNegativeButton(R.string.btn_no, null)
                        .create()
                        .show();
            }
        });
    }

    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgress();
        Log.e(TAG, "onActivityResult resultCode " + resultCode);

        switch (requestCode) {
            case LOGIN_REQUEST_CODE:

                /*
                 * launchLoginFlowAsync API 호출 시 전달받은 intent 데이터를 handleLoginData를 통하여 응답값을 파싱합니다.
                 * 파싱 이후 응답 결과를 launchLoginFlowAsync 호출 시 넘겨준 LoginFlowListener 를 통하여 전달합니다.
                 */

                if (resultCode == Activity.RESULT_OK) {
                    if (mPurchaseClient.handleLoginData(data) == false) {
                        Log.e(TAG, "onActivityResult handleLoginData false ");
                        // listener is null
                    }
                } else {
                    Log.e(TAG, "onActivityResult user canceled");

                    // user canceled , do nothing..
                }
                break;

            case PURCHASE_REQUEST_CODE:

                /*
                 * launchPurchaseFlowAsync API 호출 시 전달받은 intent 데이터를 handlePurchaseData를 통하여 응답값을 파싱합니다.
                 * 파싱 이후 응답 결과를 launchPurchaseFlowAsync 호출 시 넘겨준 PurchaseFlowListener 를 통하여 전달합니다.
                 */

                if (resultCode == Activity.RESULT_OK) {
                    if (mPurchaseClient.handlePurchaseData(data) == false) {
                        Log.e(TAG, "onActivityResult handlePurchaseData false ");
                        // listener is null
                    }
                } else {
                    Log.e(TAG, "onActivityResult user canceled");

                    // user canceled , do nothing..
                }
                break;
            default:
        }
    }
}