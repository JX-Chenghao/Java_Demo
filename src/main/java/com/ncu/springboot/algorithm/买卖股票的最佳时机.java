package com.ncu.springboot.algorithm;

import javax.validation.constraints.Max;

/**
 * 121. 买卖股票的最佳时机
 * 简单
 * 相关标签
 * 相关企业
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * <p>
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * <p>
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：[7,1,5,3,6,4]
 * 输出：5
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 * 注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 * 示例 2：
 * <p>
 * 输入：prices = [7,6,4,3,1]
 * 输出：0
 * 解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
 */
public class 买卖股票的最佳时机 {


    /**
     * f（n）代表当天卖出得到的最大利润
     * f（n） = max {f（n-1）, price[n]-MIN(price[n-1])}
     *
     *
     * 画出股票波动图，分析波峰 波谷
     *    单次买入
     *
     *    多次买入
     *    最多只允许2ci此ma买入
     */

    public static int maxProfit(int[] prices) {

        return maxProfitDG(prices, prices.length -1);
    }

    public static int maxProfitDG(int[] prices, int length) {
        if (prices.length == 0 || prices.length == 1) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        int result = 0;
        for (int i = 0; i <= length; i++) {
            min = prices[i] <= min ?  prices[i] : min;
            result = Math.max(maxProfitDG(prices, length -1), prices[i] - min);
        }
        return result;
    }

    public static void main(String[] args) {
        int i = maxProfit(new int[]{2,6,8,7,8,7,9,4,1,2,4,5,8});
        System.out.println(i);
    }
}
