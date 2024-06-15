package com.ncu.springboot;

import org.springframework.util.StringUtils;

public class Test {
    public static void main(String[] args) {
        String s = plusData("99", "999");
        System.out.println(s);

    }


    public static String plusData(String s1, String s2) {
        if (StringUtils.isEmpty(s1)) {
            return s2;
        }
        if (StringUtils.isEmpty(s2)) {
            return s1;
        }
        int length1 = s1.length();
        int length2 = s2.length();
        char[] s1Array = s1.toCharArray();//143
        char[] s2Array = s2.toCharArray();//11

        StringBuilder stringBuilder = new StringBuilder();//倒转一下
        int flag = 0;//进位
        for (int i=1 ; i<=Math.min(length1, length2); i++) {
            char c1 = s1Array[length1-i];
            char c2 = s2Array[length2-i];

            Integer i1 = Integer.valueOf(String.valueOf(c1));
            Integer i2 = Integer.valueOf(String.valueOf(c2));
            int res = i1 + i2 + flag;
            flag = res > 10 ? 1 : 0;
            stringBuilder.append(res > 10 ? res%10 : res);
        }

        if (flag > 0) {
            String extraStr = "";
            if (length1 > length2) {
                // S1的多余部分
                extraStr = s1.substring(0, length1-length2);
            } else {

                extraStr = s2.substring(0, length2-length1);
            }

            if (!StringUtils.isEmpty(extraStr)) {
                while (flag >0) {
                    char c = extraStr.charAt(extraStr.length() - 1);
                    Integer cInt = Integer.valueOf(String.valueOf(c));
                    int res = cInt + 1;
                    flag = res > 10 ? 1 : 0;
                    stringBuilder.append(res > 10 ? res%10 : res);
                }
            } else {
                stringBuilder.append("1");
            }

        }



        return stringBuilder.reverse().toString();
    }
}
