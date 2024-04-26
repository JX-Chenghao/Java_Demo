package com.ncu.springboot.algorithm;

/**
 * 整数数组 nums 按升序排列，数组中的值 互不相同 。
 *
 * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
 *
 * 给你 旋转后 的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
 *
 * 你必须设计一个时间复杂度为 O(log n) 的算法解决此问题。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,5,6,7,0,1,2], target = 0
 * 输出：4
 * 示例 2：
 *
 * 输入：nums = [4,5,6,7,0,1,2], target = 3
 * 输出：-1
 * 示例 3：
 *
 * 输入：nums = [1], target = 0
 * 输出：-1
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 5000
 * -104 <= nums[i] <= 104
 * nums 中的每个值都 独一无二
 * 题目数据保证 nums 在预先未知的某个下标上进行了旋转
 * -104 <= target <= 104
 */
public class 搜索旋转排序数组 {
    //红蓝染色法

    public static void main(String[] args) {
        int[] nums = new int[]{4,5,6,7,0,1,2};
        search(nums, 0);
    }

    public int searchV2(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while(left < right){
            int mid = left + ((right - left) >> 1);
            if(target == nums[mid]) return mid;
            if(nums[mid] < nums[right]){//有序部分为后半段，在后半段判断
                if(target > nums[mid] && target <= nums[right]){
                    left = mid + 1;
                }else{//target大于右边界 or target小于nums[mid]
                    right = mid - 1;
                }
            }else{//前半部分有序，在前半段判断
                if(target >= nums[left] && target < nums[mid]){
                    right = mid - 1;
                }else{
                    left = mid + 1;
                }
            }
        }
        return nums[left] == target ? left : -1;
    }



    public static int search(int[] nums, int target) {
        int left = -1;
        int right = nums.length-1;
        while (left + 1 < right) {
            int mid = left + (right-left)/2;
            if (isBule(nums, mid, right, target)) {
                right = mid;
            } else{
                left = mid;
            }
        }
        if (right == nums.length || nums[right] != target) {
            return -1;
        } else {
            return right;
        }

    }


    public static int searchV3(int[] nums, int target) {
        int left = 0;
        int right = nums.length-1;
        while (left < right) {
            int mid = left + (right-left)/2;
            if (nums[mid] == target) return mid;
            if (isBule(nums, mid, right, target)) {
                right = mid - 1;
            } else{
                left = mid + 1;
            }
        }
        if (right < 0 ||nums[right] != target) {
            return -1;
        } else {
            return right;
        }

    }



    public static boolean isBule(int[] nums, int mid, int right, int target) {
        if (nums[mid] <= nums[right]) {
            if (target <= nums[right] && target <= nums[mid]) {
                return true;
            } else if (target <= nums[right] && target > nums[mid]) {
                return false;
            } else { //target > nums[right]
                return true;
            }
        } else {
            if (target > nums[right] && target <= nums[mid]) {
                return true;
            } else if (target > nums[right] && target > nums[mid])  {
                return false;
            } else {//target < nums[right]
                return false;
            }
        }
    }
}
