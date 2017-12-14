package com.onestore.iap.sample.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onestore.iap.sample.R;

import java.util.List;

/**
 * Lucky ONE - 메인화면 (View)
 */

public class LuckyMainView extends LinearLayout {
    private UserCallback mListener = null;

    private LinearLayout mLlResultArea;
    private LinearLayout mLlLuckyArea;

    private Button mBtnGoSetting;
    private Button mBtnGoBuy;
    private RelativeLayout mBtnGenerateNumber;

    private TextView mTvGenerateNumberKo;
    private TextView mTvGenerateNumberEn;

    private ResultNumberView mResultNumberView;
    private LuckyNumberView mLuckyNumberView;

    private TextView mTvGameCoin;


    public LuckyMainView(Context context) {
        super(context);
        init(context);
    }

    public LuckyMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public LuckyMainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_lucky_main, this, true);

        mLlResultArea = (LinearLayout) view.findViewById(R.id.ll_result_area);
        mLlLuckyArea = (LinearLayout) view.findViewById(R.id.ll_lucky_area);

        mBtnGoSetting = (Button) view.findViewById(R.id.btn_go_setting);
        mBtnGoBuy = (Button) view.findViewById(R.id.btn_go_buy);
        mBtnGenerateNumber = (RelativeLayout) view.findViewById(R.id.btn_generate_number);

        mTvGenerateNumberKo = (TextView) view.findViewById(R.id.tv_generate_number_ko);
        mTvGenerateNumberEn = (TextView) view.findViewById(R.id.tv_generate_number_en);

        mTvGameCoin = (TextView) view.findViewById(R.id.tv_game_coin);

        mBtnGoSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goSetting();
            }
        });

        mBtnGoBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBuy();
            }
        });

        mBtnGenerateNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.generateNumber();
            }
        });
    }

    public void setData(List<Integer> luckyBall, List<Integer> myBall) {
        mLuckyNumberView = new LuckyNumberView(getContext());
        mResultNumberView = new ResultNumberView(getContext());

        mLlLuckyArea.addView(mLuckyNumberView);
        mLlResultArea.addView(mResultNumberView);

        mLuckyNumberView.setData(luckyBall);
        mResultNumberView.setData(luckyBall, myBall);
    }

    public void updateGameCoin(int coin) {
        mTvGameCoin.setText(String.format("%,d", coin));
    }

    public void updateGenerateButton(boolean isAuto) {
        if (isAuto) {
            mTvGenerateNumberKo.setText(R.string.btn_number_generate_auto_ko);
            mTvGenerateNumberEn.setText(R.string.btn_number_generate_auto_en);
        } else {
            mTvGenerateNumberKo.setText(R.string.btn_number_generate_inapp_ko);
            mTvGenerateNumberEn.setText(R.string.btn_number_generate_inapp_en);
        }
    }

    public void clearResult() {
        mLlLuckyArea.removeAllViews();
        mLlResultArea.removeAllViews();
    }

    public void setUserActionListener(UserCallback listener) {
        mListener = listener;
    }

    public interface UserCallback {
        void goSetting();

        void goBuy();

        void generateNumber();
    }
}
