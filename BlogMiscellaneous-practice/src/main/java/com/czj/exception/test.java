package com.czj.exception;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        double szzs = 3396.56;

        double szzs_1 = szzs - 100;

        double szzs_2 = szzs + 100;

        // 不足两位小数补0
        DecimalFormat decimalFormat = new DecimalFormat("0.00#");

        System.out.println(szzs > 3396);
        System.out.println(szzs_1 + "  " + szzs_2);
        StringBuilder stringBuilder = new StringBuilder();

        BigDecimal bigDecimal = new BigDecimal(szzs_1);
        List<String> szzslist = new ArrayList<>();
        BigDecimal zero = new BigDecimal(0.01);
        while (bigDecimal.compareTo(new BigDecimal(3496.56)) <= -1) {
            String format = decimalFormat.format(bigDecimal.setScale(2, BigDecimal.ROUND_UP).floatValue());
            stringBuilder.append(format).append(",");
            szzslist.add(format);
            bigDecimal = bigDecimal.add(zero);
        }
        System.out.println(stringBuilder.toString());
        System.out.println(szzslist.size());

        for (int i = 0; i < 15; i++) {
            System.out.println(szzslist.get((int) (Math.random() * 20000)));
        }



    }
}
