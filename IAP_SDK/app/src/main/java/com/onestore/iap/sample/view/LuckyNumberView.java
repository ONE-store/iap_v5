package com.onestore.iap.sample.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onestore.iap.sample.R;

import java.util.List;

/**
 * Lucky ONE - 메인화면 내부 UI Component (View)
 */

public class LuckyNumberView extends LinearLayout {
    private ImageView[] mIvNumbers;
    private TextView[] mTvNumbers;

    public LuckyNumberView(Context context) {
        super(context);
        init(context);
    }

    public LuckyNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public LuckyNumberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_lucky_ball, this, true);

        mIvNumbers = new ImageView[6];
        mIvNumbers[0] = (ImageView) view.findViewById(R.id.iv_first_number);
        mIvNumbers[1] = (ImageView) view.findViewById(R.id.iv_second_number);
        mIvNumbers[2] = (ImageView) view.findViewById(R.id.iv_third_number);
        mIvNumbers[3] = (ImageView) view.findViewById(R.id.iv_fourth_number);
        mIvNumbers[4] = (ImageView) view.findViewById(R.id.iv_fifth_number);
        mIvNumbers[5] = (ImageView) view.findViewById(R.id.iv_sixth_number);

        mTvNumbers = new TextView[6];
        mTvNumbers[0] = (TextView) view.findViewById(R.id.tv_first_number);
        mTvNumbers[1] = (TextView) view.findViewById(R.id.tv_second_number);
        mTvNumbers[2] = (TextView) view.findViewById(R.id.tv_third_number);
        mTvNumbers[3] = (TextView) view.findViewById(R.id.tv_fourth_number);
        mTvNumbers[4] = (TextView) view.findViewById(R.id.tv_fifth_number);
        mTvNumbers[5] = (TextView) view.findViewById(R.id.tv_sixth_number);
    }

    public void setData(List<Integer> luckyBall) {

        for (int i = 0; i < luckyBall.size(); i++) {
            mIvNumbers[i].setBackgroundResource(getBgResourceId(luckyBall.get(i)));
            mTvNumbers[i].setText(Integer.toString(luckyBall.get(i)));
        }
    }

    private int getBgResourceId(int number) {

        if (number <= 10) {
            return R.drawable.ball_1;
        } else if (number <= 20) {
            return R.drawable.ball_10;
        } else if (number <= 30) {
            return R.drawable.ball_20;
        } else if (number <= 40) {
            return R.drawable.ball_30;
        } else {
            return R.drawable.ball_40;
        }
    }
}
