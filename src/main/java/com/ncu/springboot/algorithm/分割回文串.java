package com.ncu.springboot.algorithm;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是
 * 回文串
 *  。返回 s 所有可能的分割方案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 * 示例 2：
 *
 * 输入：s = "a"
 * 输出：[["a"]]
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 16
 * s 仅由小写英文字母组成
 */
public class 分割回文串 {

    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        if (StringUtils.isEmpty(s)) {
            return res;
        }
        if (s.length() <= 1) {
            List<String> path = new ArrayList<>();
            path.add(s);
            res.add(path);
            return res;
        }

        sub(s, 0 ,new ArrayList<>() , res);

        return res;
    }

    /**
     * 从答案判断
     * @param s
     * @param i
     * @param res
     */
    static void  subV2(String s, int i, List<String> path , List<List<String>> res) {
        if (i == s.length()) {
            res.add(copy(path));
            return;
        }
        char[] charArray = s.toCharArray();
        for (int j = i ; j < s.length();j++) {
            if (judgeHuiWenStr(charArray, i, j)) {
                path.add(String.valueOf(charArray,i, j-i+1));
                int temp = j+1;
                subV2(s, temp, path, res);
                path.remove(path.size() -1);
            }
            //sub(s, temp, path, res);
        }
    }


    /**
     * 从输入判断
     * @param s
     * @param i
     * @param res
     */
    static void  sub(String s, int i, List<String> path , List<List<String>> res) {
        if (i == s.length()) {
            res.add(copy(path));
            return;
        }
        char[] charArray = s.toCharArray();
        for (int j = i ; j < s.length();j++) {
            if (judgeHuiWenStr(charArray, i, j)) {
                path.add(String.valueOf(charArray,i, j-i+1));
                sub(s, j+1, path, res);
                path.remove(path.size() -1);
            }
            
        }
    }

    public static List<String> copy(List<String> path) {
        List<String> copy = new ArrayList<>();
        for (String str : path) {
            copy.add(str);
        }
        return copy;
    }

    public static boolean judgeHuiWenStr(char[] charArray, int l, int r) {
        if (l > r) {
            return false;
        }
        /**
         * 左右指针向内判断
         */
        for (; l < r ; l++) {
            if (charArray[l] != charArray[r]) {
                return false;
            }
            r--;
        }

        return true;
    }

    public static void main(String[] args) {
        boolean b = judgeHuiWenStr("sassasd".toCharArray(), 0, 5);
        System.out.println(b);

        List<List<String>> res = partition("aab");
        System.out.println(res);



    }
}
