package com.ncu.springboot.algorithm;

import jodd.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
 *
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 *
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 *
 * 输入：head = [1,2,3,4,5], k = 2
 * 输出：
 */
public class K个一组翻转链表 {
    public ListNode reverseKGroup(ListNode head, int k) {

        return head;
    }

    public static void main(String[] args) {
        K个一组翻转链表 x = new K个一组翻转链表();
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode listNode3 = new ListNode(3);
        ListNode listNode4 = new ListNode(4);
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode4;
        /**
         * 链表反转
         */
       // ListNode listNode = x.reverseList(listNode1);
       // System.out.println(listNode);

        /**
         * 截取部分链表反转
         */
        ListNode test = x.reverseList(listNode1, listNode3);
        System.out.println(test);

        // k个一组

    }


    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;
        ListNode next = head.next;
        while (next != null) {
            cur.next = pre;
            pre = cur;
            cur = next;
            next = cur.next;
        }
        cur.next = pre;
        return cur;
    }

    public ListNode reverseList(ListNode head, ListNode tail) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;
        ListNode next = head.next;
        while (cur != tail) {
            cur.next = pre;
            pre = cur;
            cur = next;
            next = cur.next;
        }
        cur.next = pre;
        return cur;
    }


      private static class ListNode {
          int val;
          ListNode next;
          ListNode() {}
          ListNode(int val) { this.val = val; }
          ListNode(int val, ListNode next) { this.val = val; this.next = next; }

          @Override
          public String toString() {
              return val + "";
          }
      }
}
