package com.noob.storage.utils.print.element;

import java.util.Collections;
import java.util.List;

/**
 * @author luyun
 * @version 理财计划
 * @since 2017.11.03
 */
public class Row implements ElementInterface, Comparable<Row> {

    private int row;

    /**
     * 这一行包含的所有元素
     */
    private List<TableElement> elementList;

    @Override
    public String toHtml(int maxCols) {
        Collections.sort(elementList);
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        int printedCols = 0;
        for (int i = 0; i < elementList.size(); i++) {
            TableElement e = elementList.get(i);
            if (i == elementList.size() - 1) {
                sb.append(e.toHtml(true, printedCols, maxCols));
            } else {
                sb.append(e.toHtml(false, printedCols, maxCols));
            }
            printedCols += e.getCols();
        }
        sb.append("</tr>");
        return sb.toString();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public List<TableElement> getElementList() {
        return elementList;
    }

    public void setElementList(List<TableElement> elementList) {
        this.elementList = elementList;
    }

    @Override
    public int getCols() {
        int cols = 0;
        for (TableElement e : elementList) {
            cols += e.getCols();
        }
        return cols;
    }

    @Override
    public int getRows() {
        int row = 0;
        for (TableElement e : elementList) {
            if (e.getRows() > row) {
                row = e.getRows();
            }
        }
        return row;
    }


    @Override
    public int compareTo(Row o) {
        return row - o.row;
    }
}
