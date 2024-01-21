package com.ncu.springboot.algorithm;

/*
* TOP-K问题。
求TOP-K问题最简单的方式为快速排序后取前K大的即可。但是这样做有两个问题

1. 快速排序的平均复杂度为 nlogn，但最坏时间复杂度为 n2 。

2. 我们只需要前 [公式] 大的，而对其余不需要的数也进行了排序，浪费了大量排序时间。

而堆排序也是一个较好的方法，维护一个大小为 k的堆，时间复杂度为 nlogk。


Bfprt 用来获取第K大 ，k小的数
再遍历一边 可以拿到topK
*
*
* */
public class BfprtAlgorithm {

    //寻找中位数的中位数
    int getMiddleNumOfMiddles(int[] arr, int left, int right)
    {
        if(left == right) return left;
        int i;
        int n = 0;
        for(i = left; i < right- 5; i += 5)
        {
            insertSort(arr, i, i + 4);// 0 4 / 5 9/
            n = i - left;
            swap(arr,left + n / 5, i + 2);
        }
        //处理剩余元素
        int num = right - i + 1;
        if(num > 0)
        {
            insertSort(arr, i, right);
            n = i - left;
            swap(arr,left + n / 5,i + num / 2);
        }
        n /= 5;
        if(n == left) return left;
        return getMiddleNumOfMiddles(arr, left, left + n);
    }


    int partition(int[] arr, int left, int right,int pivot) {
        if (arr == null || right - left < 0) return 0;
        if (right - left == 0) return left;
        //int low = -1;
        //int high = arr.length;
        int low = left -1;
        int high = right + 1;
        int i = left;
        while (i < high) {
            if (arr[i] < pivot) {
                low++;
                swap(arr, low, i);
                i++;
            } else if (arr[i] > pivot) {
                high--;
                swap(arr, high, i);
            } else {
                i++;
            }
        }
        return i-1;
    }


    private static void swap(int[] objs, int a, int b) {
        if (a == b) return;
        objs[a] = objs[a] ^ objs[b];
        objs[b] = objs[a] ^ objs[b];
        objs[a] = objs[a] ^ objs[b];
    }




    static void insertSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {//第二个往后逐个向前比较 选择合适位置插入
            int temp = arr[i];//用作比较的数据
            int j = i - 1;
            while (j >= left && temp < arr[j]) {//当比到最左边或者遇到比temp小的数据时，结束循环
                arr[j + 1] = arr[j];//顺序后移
                j--;
            }
            arr[j + 1] = temp;//把temp放到空位上
        }

    }



    public static void main(String[] args){
        int [] a ={12,45,121,5,4,51,21,5,8,12,46,121,54,34};

        //第k 小的数
        int kthNumbers = new BfprtAlgorithm().BFPRT(a,0 ,a.length-1, 10);

        //再一个遍历O(n) 拿到top k 数组
        System.out.println("Chenghao--"+kthNumbers);
    }

    int BFPRT(int a[], int l, int r, int k) {
        int p = getMiddleNumOfMiddles(a, l, r);    //寻找中位数的中位数
        int i = partition(a, l, r, a[p]);

        int m = i - l + 1;
        if(m == k) return a[i];
        if(m > k)  return BFPRT(a, l, i-1, k);
        return BFPRT(a, i + 1, r, k - m);
    }

}
