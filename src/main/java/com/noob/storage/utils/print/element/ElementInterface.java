package com.noob.storage.utils.print.element;

/**
 * @author luyun
 * @version 理财计划
 * @since 2017.11.03
 */
public interface ElementInterface {

    /**
     * 当前元素占了几列
     */
    int getCols();

    /**
     * 当前元素占了几行
     */
    int getRows();

    /**
     * 转换成Html
     * maxCols 最大列数
     */
    String toHtml(int maxCols);
}
