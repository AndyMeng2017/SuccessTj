package com.hcicloud.sap.study.thread;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class Memorizer {

    public void test() {
        int i = 0;
        while (true){

            System.out.println("进入方法了===================="+i);
            Scanner out = new Scanner(System.in);
            System.out.println("输入一个int型数据：");
            int a= out.nextInt();
            System.out.println(a);
        }
    }
}
