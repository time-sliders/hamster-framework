package com.noob.storage.thread.schedule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 比重分配策略
 * 根据每一个持有者定义的比重，在所有持有者比重总和中的占比，来分配所有资源
 */
public class ProportionAllocatePolicy implements ResourceAllocatePolicy {

    private static Comparator<Holder> comparator = new Comparator<Holder>() {
        public int compare(Holder h1, Holder h2) {
            return h2.getNeedForReSource() - h1.getNeedForReSource();
        }
    };

    public void allocate(int maxnum, List<Holder> holders) {
        if (holders == null || holders.isEmpty() || maxnum <= 0) {
            return;
        }
        int sumProportion = 0;
        int idleNum = maxnum;
        int[] allocationResult = new int[holders.size()];
        Collections.sort(holders, comparator);
        for (Holder holder : holders) {
            sumProportion += holder.getNeedForReSource();
        }
        for (int i = 0; i < holders.size() && idleNum > 0; i++) {
            Holder holder = holders.get(i);
            int allocateNum = (int) ((holder.getNeedForReSource() / (sumProportion * 1.0)) * maxnum);
            idleNum -= allocateNum;
            allocationResult[i] += allocateNum;
            /**分配完毕之后idle的资源 按照比重顺序加到持有者手上*/
            if (i >= (holders.size() - 1) && idleNum > 0) {
                for (int j = 0; j < allocationResult.length && idleNum > 0; j++) {
                    allocationResult[j]++;
                    idleNum--;
                }
            }
        }
        for (int i = 0; i < holders.size(); i++) {
            if (allocationResult[i] > 0) {
                holders.get(i).reInit(allocationResult[i]);
            }
        }
    }
}