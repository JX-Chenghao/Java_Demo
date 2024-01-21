package com.ncu.springboot.algorithm.sort;

import com.ncu.springboot.pojo.BaseBo;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class SortUtil<T extends BaseBo> {
    public  List<T> bubblingSort(List<T> objs) {
        for (int i = 0 ; i < objs.size() ; i++) {
            for (int j = i+1 ; j< objs.size(); j++) {
                if (objs.get(i).getId() < objs.get(j).getId()) {
                    swap(objs, i, j);
                }
            }
        }
        return objs;
    }

    public  List<T> choosingSort(List<T> objs) {
        if (CollectionUtils.isEmpty(objs)) return null;
        Long max = objs.get(0).getId();
        Integer maxIndex = 0;
        for (int i = 0 ; i < objs.size() ; i++) {
            for (int j = i+1 ; j< objs.size(); j++) {
                if (objs.get(j).getId() > max) {
                    max = objs.get(j).getId();
                    maxIndex = j;
                }
            }
            swap(objs, i, maxIndex);
        }
        return objs;
    }


    private void swap(List<T> objs, int a, int b) {
        T temp = objs.get(a);
        objs.set(a, objs.get(b));
        objs.set(b, temp);
    }


    private void swap(int[] objs, int a, int b) {
        if (a == b) return;
        objs[a] = objs[a]^objs[b];
        objs[b] = objs[a]^objs[b];
        objs[a] = objs[a]^objs[b];
    }





    public int[] mergingSort (int [] arr) {
        if (arr==null || arr.length == 0) return null;
        mergingSort(arr,0,arr.length);
        return arr;
    }

    private void mergingSort (int [] arr,int i, int j) {
        if (arr.length == 1) return;
        if (arr.length == 2) {
            if (arr[i] > arr[j]) swap(arr,i,j);
        } else {
            int medium = (i + j)/2;
            mergingSort(arr,i,medium);
            mergingSort(arr,medium,j);
            merge(arr, i, medium, medium + 1, j);
        }
    }

    private void merge(int [] arr,int i, int j,int a ,int b){
        int len1 = j-i+1;
        int len2 = b-a+1;
        int[] res = new int[len1+len2];
        int x = 0;
        int y = len1>len2 ? len2 : len1;
        for (; x < y; x++) {
            if (arr[i]<arr[j]) {
                res[x++] =arr[i++];
            } else {
                res[x++] =arr[a++];
            }
        }
        if (i>j) {
            for ( ; a < b;a++) {
                res[x] =arr[a];
                x++;
            }
        } else {
            for ( ; i < j;i++ ) {
                res[x] =arr[i];
                x++;
            }
        }
        for (x = 0; x < res.length; x++) {
            arr[i + x] = res[x];
        }
    }

    public void heapSort(int[] arr) {
        for (int i = 0 ; i < arr.length ;i++) {
            heapInsert(arr,i);//构建一个最大堆
        }
        for (int j = arr.length-1 ; j > 0 ; j--) {
            swap(arr, 0 , j);
            heapify(arr,0, j);
        }

    }

    private void heapInsert(int[] arr, int index) {
        int temp = arr[index];
        while (temp > arr[(index-1)/2]) {
            swap(arr,index,(index-1)/2);
            index = (index-1)/2;
        }
    }

    public  void heapify(int[] arr, int index, int size) {
        int left = index * 2 + 1;
        while (left < size) {//往左子树遍历
            if (left + 1 == size) {
                break;
            }
            int largest = arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index; //比父亲
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);//大的为父节点
            index = largest;//变动的左或右节点 需要再调整它作为父节点的树，不变的右或左节点为父节点的树结构稳定，为大根子堆，不需再变动，
            left = index * 2 + 1;
        }
    }

    // for test
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // for test
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int [] arr ={12,45,121,5,4,51,21,5,8,12,46,121,54,34};
        // sorted : { 1, 1, 1, 1, 2, 2, 2, 3, 3, 5, 5, 5, 6, 6, 6, 7, 9, 9, 9 }
        new SortUtil<>().heapSort(arr);
        printArray(arr);

    }

}
