package com.company;

public class ArrClass implements Runnable{
    private final int dim;
    private int partNum;

    public final int[] arr;

    public ArrClass(int dim) {
        this.dim = dim;
        arr = new int[dim];
        for(int i = 0; i < dim; i++){
            arr[i] = i;
        }
    }

    public long partSum(Bounds bounds){
        long sum = 0;
        for(int i = bounds.startIndex(); i < bounds.finishIndex(); i++){
            sum += arr[i];
        }
        return sum;
    }

    private long sum = 0;

    synchronized private long getSum() {
        while (finishedPartCount < partNum){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    synchronized public void collectSum(long sum){
        this.sum += sum;
    }

    private int finishedPartCount = 0;

    synchronized public void incPartCount(){
        finishedPartCount++;
        notify();
    }

    private int currentPartNum = 0;
    private int partLength;

    synchronized public Bounds getNextBounds(){
        int startIndex = 0;
        int finishIndex = dim;
        boolean goodBounds = false;

        if (currentPartNum < partNum){
            goodBounds = true;
            startIndex = currentPartNum * partLength;
            currentPartNum++;
            if (currentPartNum < partNum) {
                finishIndex = currentPartNum * partLength;
            }
        }

        return new Bounds(startIndex, finishIndex, goodBounds);
    }

    public long threadSum(int threadNum, int partNum){
        this.partNum = partNum;
        partLength = arr.length / partNum;

        for (int i = 0; i < threadNum; i++) {
            new Thread(this).start();
        }

        return getSum();
    }

    @Override
    public void run() {
        Bounds bounds = getNextBounds();
        while (bounds.goodBounds()) {
            long sum = partSum(bounds);
            collectSum(sum);
            incPartCount();
            bounds = getNextBounds();
        }
    }
}