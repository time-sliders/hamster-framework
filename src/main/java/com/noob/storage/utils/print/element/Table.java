package com.noob.storage.utils.print.element;

import java.util.Collections;
import java.util.List;

/**
 * @author luyun
 * @version 理财计划
 * @since 2017.11.03
 */
public class Table implements ElementInterface {

    /**
     * 表格包含的所有行
     */
    private List<Row> rowList;

    @Override
    public String toHtml(int maxCols) {
        Collections.sort(rowList);
        StringBuilder sb = new StringBuilder();
        //style="font-family:verdana;color:red"
        sb.append("<html>" +
                "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head>" +
                "<table style=\"border-collapse:collapse\" align=\"center\" width=\"800\" border=\"5\" bordercolor=\"gray\">");
        for (Row row : rowList) {
            sb.append(row.toHtml(maxCols));
        }
        sb.append("</table></html>");
        return sb.toString();
    }

    public List<Row> getRowList() {
        return rowList;
    }

    public void setRowList(List<Row> rowList) {
        this.rowList = rowList;
    }

    @Override
    public int getCols() {
        int cols = 0;
        for (Row e : rowList) {
            if (e.getCols() > cols) {
                cols = e.getCols();
            }
        }
        return cols;
    }

    @Override
    public int getRows() {
        int row = 0;
        for (Row e : rowList) {
            row += e.getRows();
        }
        return row;
    }


}
