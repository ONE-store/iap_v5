package com.onestore.iap.sample.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Lucky ONE - 게임 동작을 위한 Util 클래스
 */

public class LuckyUtils {
    private static final String TAG = LuckyUtils.class.getSimpleName();

    public static List<List<Integer>> getSuggestNumbers() {
        List<List<Integer>> suggestedNumbersList = new ArrayList<>(5);
        suggestedNumbersList.clear();
        for (int j = 0; j < 5; j++) {
            suggestedNumbersList.add(createRandomNumberList());
        }
        return suggestedNumbersList;
    }

    public static List<Integer> getMyNumbers() {
        return createRandomNumberList();
    }

    private static List<Integer> createRandomNumberList() {
        List<Integer> numberList = new ArrayList<>();
        Random rand = new Random();

        while (numberList.size() < 6) {
            int number = rand.nextInt(45) + 1;
            if (numberList.contains(number)) {//중복 발생
                continue;
            }

            numberList.add(number);
        }

        Collections.sort(numberList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 < o2
                        ? -1
                        : o1 > o2 ? 1 : 0;
            }
        });

        Log.d(TAG, "createRandomNumberList numberList " + numberList);
        return numberList;
    }

    public static int getWonCoin(List<Integer> luckyBall, List<List<Integer>> myBallList) {
        int totalCoin = 0;

        for (List<Integer> myBalls : myBallList) {
            int lineCount = 0;

            for (Integer lucky : luckyBall) {
                for (Integer my : myBalls) {
                    if (lucky == my) {
                        lineCount++;
                    }
                }
            }
            totalCoin = totalCoin + getLuckyCoin(lineCount);
        }
        return totalCoin;
    }

    private static int getLuckyCoin(int number) {
        switch (number) {
            case 3:
                return 5;
            case 4:
                return 30;
            case 5:
                return 100;
            case 6:
                return 300;
            default:
                return 0;
        }
    }
}
