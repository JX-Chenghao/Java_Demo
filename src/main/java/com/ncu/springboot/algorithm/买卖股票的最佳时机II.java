package com.ncu.springboot.algorithm;

/**
 * dfs(i) + 卖出 = max dfs(i-1) + 卖出  + dfs（i-1）+持有
 *
 *
 */
public class 买卖股票的最佳时机II {

    /**
     *
     * dfs (i,j,k)
     *
     *
     */
















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
