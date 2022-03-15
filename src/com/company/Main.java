package com.company;

public class Main {

    public static void main(String[] args) {
        int dim = 10000000;
        int threadNum = 4;
        int partNum = 8;
        ArrClass arrClass = new ArrClass(dim);
        System.out.println(arrClass.partSum(new Bounds(0, dim,true)));

        System.out.println(arrClass.threadSum(threadNum, partNum));
    }
}
