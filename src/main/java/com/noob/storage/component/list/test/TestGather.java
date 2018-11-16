package com.noob.storage.component.list.test;

import com.noob.storage.component.list.GatherMode;
import com.noob.storage.component.list.SortedCollectionIterator;
import com.noob.storage.component.list.SortedCollectionsGather;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luyun
 * @since 2018.11.15 10:43
 */
public class TestGather {

    public static void main(String[] args) {
        SortedCollectionsGather<Integer, Integer, Integer> gather = new SortedCollectionsGather<Integer, Integer, Integer>() {
            @Override
            protected boolean isRepeatingValue(Integer o1, Integer o2) {
                return o1.equals(o2);
            }

            @Override
            protected boolean isFinished(List<Integer> resultList, Integer request) {
                return resultList.size() >= 10;
            }
        };

        SortedCollectionIterator<Integer, Integer, Integer, Integer, Integer> i1 =
                new SortedCollectionIterator<Integer, Integer, Integer, Integer, Integer>() {
                    private int idx = 0;

                    @Override
                    protected List<Integer> query(Integer query) {
                        idx++;
                        if (idx == 1) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(1);
                            list.add(3);
                            list.add(5);
                            list.add(6);
                            return list;
                        } else if (idx == 2) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(8);
                            list.add(10);
                            list.add(12);
                            list.add(13);
                            return list;
                        }
                        return null;
                    }

                    @Override
                    protected boolean checkValidation(Integer integer, Integer integer2) {
                        return integer2 < 2;
                    }

                    @Override
                    public Integer getCurrentCompareValue() {
                        return currentElement;
                    }

                    @Override
                    protected Integer initPrivateQueryParam(Integer integer) {
                        return null;
                    }

                    @Override
                    protected void buildNextPageQueryParam(Integer prePageQueryParam, List<Integer> prePageDataList) {
                    }

                    @Override
                    public Integer convertCurrentValueToResultModel() {
                        return currentElement;
                    }
                };

        SortedCollectionIterator<Integer, Integer, Integer, Integer, Integer> i2 =
                new SortedCollectionIterator<Integer, Integer, Integer, Integer, Integer>() {
                    private int idx = 0;

                    @Override
                    protected List<Integer> query(Integer query) {
                        idx++;
                        if (idx == 1) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(2);
                            list.add(5);
                            list.add(6);
                            list.add(9);
                            return list;
                        } else if (idx == 2) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(11);
                            list.add(14);
                            list.add(15);
                            list.add(16);
                            return list;
                        }
                        return null;

                    }

                    @Override
                    protected boolean checkValidation(Integer integer, Integer integer2) {
                        return integer2 < 3;
                    }

                    @Override
                    public Integer getCurrentCompareValue() {
                        return currentElement;
                    }

                    @Override
                    protected Integer initPrivateQueryParam(Integer integer) {
                        return null;
                    }

                    @Override
                    protected void buildNextPageQueryParam(Integer prePageQueryParam, List<Integer> prePageDataList) {
                    }

                    @Override
                    public Integer convertCurrentValueToResultModel() {
                        return currentElement;
                    }
                };

        gather.addIterator(i2);
        gather.addIterator(i1);
        gather.setGatherMode(GatherMode.LEAST_QUERY_MODE);
        gather.setDistinct(true);

        List<Integer> list = gather.gather(0);

        System.out.println(list.size() + ">>>>>>");
        for (Integer i : list) {
            System.out.println(i);
        }
    }

}
