package com.hcicloud.sap.study.offer;

/**
 * 题目描述【1.二维数组中的查找】
 * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 *
 *
 * 解答：
 * 从左下角元素往上查找，右边元素是比这个元素大，上边元素比这个元素小。
 * 于是target比这个元素小，就往上找，target比这个元素大，就往右找。如果出了边界，则说明二维数组中不存在target元素。
 */
public class Offer001 {

    public boolean find(int target, int [][] array) {
        //数组值arr[x][y]表示指定的是第x行第y列的值。
        //在使用二维数组对象时，注意length所代表的长度，
        //数组名后直接加上length(如arr.length)，所指的是有几行(Row)；
        int rows = array.length;
        //指定索引后加上length(如arr[0].length)，指的是该行所拥有的元素，也就是列(Column)数目。
        int cols = array[0].length;

        int i = rows - 1, j = 0;
        while(i >= 0 && j < cols){
            if (target > array[i][j]){
                j++;
            }
            else if (target < array[i][j]){
                i--;
            }
            else
                return true;
        }
        return false;
    }
}
