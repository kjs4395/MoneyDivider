package com.kakaopay.divider.money.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created By kjs4395 on 2020-11-21
 */
public class MoneyDivider {

    /**
     * 뿌리기 한 전체 금액을 인원수에 맞게 나눔
     *
     * @return
     */
    public static List<Integer> divideMoney(int amount, int divideNum) {
        Random random = new Random();
        List<Integer> divideList = new ArrayList<>();

        for (int i = 0; i < divideNum; i++) {
            int divide;
            if (i < divideNum - 1) {
                divide = random.nextInt(amount);
                amount -= divide;
            } else {
                divide = amount;
            }

            divideList.add(divide);
        }
        return divideList;
    }

}
