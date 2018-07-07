package com.hcicloud.sap.study.offer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 题目描述【3.从尾到头打印链表】
 * 输入一个链表，从尾到头打印链表每个节点的值。
 *
 *
 * 解答：
 * 1.递归方式实现
 * 2.借助堆栈的“后进先出”实现
 */
public class Offer003 {

    ArrayList<Integer> arrayList = new ArrayList<>();


    public class ListNode {
        int val;
        ListNode next = null;
        ListNode(int val) {
            this.val = val;
        }
    }


    //1.递归方式实现
    public ArrayList<Integer> printListFromTailToHead1(ListNode listNode) {
        if (listNode != null){
            this.printListFromTailToHead1(listNode.next);
            arrayList.add(listNode.val);
        }
        return arrayList;
    }

    //2.借助堆栈的“后进先出”实现
    public ArrayList<Integer> printListFromTailToHead2(ListNode listNode) {
        Stack<Integer> stack = new Stack<>();
        while (listNode != null){
            stack.push(listNode.val);
            listNode = listNode.next;
        }
        while (!stack.empty()){
            arrayList.add(stack.pop());
        }
        return arrayList;
    }
}
