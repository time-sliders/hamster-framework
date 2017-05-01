package com.noob.storage.concurrent;


import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.04.30
 */
public class FillAndEmpty {

    static Exchanger<List<Integer>> exchanger = new Exchanger<>();
    static List<Integer> initialEmptyList = new LinkedList<>();
    static List<Integer> initialFullList = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        initialFullList.add(0);
        new Thread(new FillingLoop()).start();
        //new Thread(new EmptyingLoop()).start();
    }


    static class FillingLoop implements Runnable {
        List<Integer> currentList = initialEmptyList;
        private int fillIndex = 1;
        @Override
        public void run() {
            System.out.println("FillingLoop started");
            while (CollectionUtils.isEmpty(currentList)) {
                fillToList();
                try {
                    System.out.println("FillingLoop fill success and waitting exchange");
                    currentList = exchanger.exchange(currentList);
                    System.out.println("FillingLoop exchange success");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void fillToList() {
            for (int i = 0; i < 5; i++) {
                currentList.add(fillIndex++);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class EmptyingLoop implements Runnable {
        List<Integer> currentList = initialFullList;

        @Override
        public void run() {
            System.out.println("EmptyingLoop started");
            while(CollectionUtils.isNotEmpty(currentList)){
                takeFromList(currentList);
                try {
                    System.out.println("EmptyingLoop clear success and waitting exchange");
                    currentList = exchanger.exchange(currentList);
                    System.out.println("EmptyingLoop exchange success");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void takeFromList(List<Integer> currentList) {
            for(Integer i : currentList){
                System.out.print(i+"_");
            }
            System.out.println();
            currentList.clear();
        }
    }

}
