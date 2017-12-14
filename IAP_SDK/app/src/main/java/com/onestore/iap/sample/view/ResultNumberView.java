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

public class ResultNumberView extends LinearLayout {
    private TextView mTvRank;
    private ImageView[] mIvNumbers;
    private TextView[] mTvNumbers;

    public ResultNumberView(Context context) {
        super(context);
        init(context);
    }

    public ResultNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public ResultNumberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_result_ball, this, true);
        mTvRank = (TextView) view.findViewById(R.id.tv_rank);
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

    public void setData(final List<Integer> luckyBall, final List<Integer> myBall) {
        int count = 0;

        for (int i = 0; i < myBall.size(); i++) {
            mTvNumbers[i].setText(Integer.toString(myBall.get(i)));
            mIvNumbers[i].setBackgroundResource(getBgResourceId(myBall.get(i)));

            for (Integer lucky : luckyBall) {
                if (lucky == myBall.get(i)) {
                    count++;
                    mIvNumbers[i].setBackgroundResource(getLuckyBgResourceId(myBall.get(i)));
                }
            }
        }

        mTvRank.setText(getLuckyCount(count));
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

    private int getLuckyBgResourceId(int number) {

        if (number <= 10) {
            return R.drawable.ball_1_line;
        } else if (number <= 20) {
            return R.drawable.ball_10_line;
        } else if (number <= 30) {
            return R.drawable.ball_20_line;
        } else if (number <= 40) {
            return R.drawable.ball_30_line;
        } else {
            return R.drawable.ball_40_line;
        }
    }

    private String getLuckyCount(int number) {

        switch (number) {
            case 3:
                return "4th";
            case 4:
                return "3rd";
            case 5:
                return "2nd";
            case 6:
                return "1st";
            default:
                return "-";
        }
    }
}
