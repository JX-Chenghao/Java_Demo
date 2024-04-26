package com.ncu.springboot.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 *
 * 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的
 * 子序列
 * 。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [10,9,2,5,3,7,101,18]
 * 输出：4
 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 * 示例 2：
 *
 *
 * 输入：nums = [0,1,0,3,2,3]
 * 输出：4
 * 示例 3：
 *
 * 输入：nums = [7,7,7,7,7,7,7]
 * 输出：1
 *
 */
public class 最长递增子序列 {
    public static void main(String[] args) {
        int i = lengthOfLISAB(new int[]{10,9});
        System.out.println(i);
    }
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        int answer = 0;
        Map<Integer, Integer> cache = new HashMap<>();


        for (int i = nums.length-1 ; i>=0; i--) {
            answer= Math.max(dfs(nums, i, cache), answer);
        }

        return answer;
    }

    public static int lengthOfLISAB(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        Map<Integer, Integer> cache = new HashMap<>();
        List<Integer> resList = new ArrayList<>();

        return dfsAB(nums, nums.length-1, Integer.MAX_VALUE, cache, resList);
    }


    /**
     * dfs(i) = max{dfs(j)} + 1     j<i  nums[j]<nums[i]  枚举
     * @param nums
     * @param idx
     * @param cache
     * @return
     */
    public static int dfs(int[] nums, int idx,Map<Integer, Integer> cache) {
        if (idx == 0) {
            return 1;
        }
        if (cache.get(idx) != null) {
            return cache.get(idx);
        }
        int res = 0;
        for (int i = idx-1 ; i>=0; i--) {
            if (nums[i] < nums[idx]) {
                int newRes = dfs(nums, i, cache);
                res= Math.max(res,newRes);
                cache.put(i, newRes);
            }
        }
        return res+1;
    }

    /**
     * 选还是不选 1 3 5 2 9 0 3 7 4
     *   选 dfs（i,MAX_int）  = dfs (j, nums[i])+1     nums[j] < nums[i]中最后一个递增数字
     *   不选 dfs（i,MAX_int）  = dfs (j, MAX_int)
     */

    //[0,1,0,3,2,3]
    public static int dfsAB(int[] nums, int idx, int pre, Map<Integer, Integer> cache,List<Integer> resList) {
        if (idx == 0) {
            return nums[idx] < pre ? 1 : 0;
        }
        int res = 0;
        int new_res = 0;
        for (int j = idx-1 ; j >= 0 ; j--) {
            if (nums[j] < pre) {
                //xuan
                res = Math.max(res, dfsAB(nums, j, nums[idx], cache, resList) + 1)  ;
                //buxuan
                res =  Math.max(dfsAB(nums, j, pre, cache, resList), res);
            } else {
                //buxuan
                res =  Math.max(dfsAB(nums, j, pre, cache, resList), res);
            }
        }
        return res;
    }


}
