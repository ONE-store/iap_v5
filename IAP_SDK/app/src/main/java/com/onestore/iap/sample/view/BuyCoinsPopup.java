package com.onestore.iap.sample.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onestore.iap.sample.R;
import com.onestore.iap.sample.util.AppConstant;

/**
 * Lucky ONE - 아이템 구매 선택 화면
 */

public class BuyCoinsPopup extends Dialog {

    private ImageButton mIbClose;
    private TextView mBtnBuyCoin5;
    private TextView mBtnBuyCoin10;
    private TextView mBtnBuyCoin60;
    private RelativeLayout mRlBuyCoinAuto;

    private UserCallback mUserCallback;

    public BuyCoinsPopup(Context context, UserCallback callback) {
        super(context);

        this.mUserCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_buy_coins);
        setCanceledOnTouchOutside(false);

        int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.90);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        mIbClose = (ImageButton) findViewById(R.id.ib_close);
        mBtnBuyCoin5 = (TextView) findViewById(R.id.btn_buy_coin_5);
        mBtnBuyCoin10 = (TextView) findViewById(R.id.btn_buy_coin_10);
        mBtnBuyCoin60 = (TextView) findViewById(R.id.btn_buy_coin_60);
        mRlBuyCoinAuto = (RelativeLayout) findViewById(R.id.rl_buy_coin_auto);

        mIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnBuyCoin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserCallback.buyCoin(AppConstant.PRODUCT_INAPP_5000);
            }
        });

        mBtnBuyCoin10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserCallback.buyCoin(AppConstant.PRODUCT_INAPP_10000);

            }
        });

        mBtnBuyCoin60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserCallback.buyCoin(AppConstant.PRODUCT_INAPP_50000);

            }
        });

        mRlBuyCoinAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserCallback.buyCoin(AppConstant.PRODUCT_AUTO_100000);

            }
        });
    }

    public interface UserCallback {
        void buyCoin(String item);
    }
}
