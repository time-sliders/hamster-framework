package com.noob.storage.utils.print.element;


import com.noob.storage.utils.print.TableLocation;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author luyun
 * @version 理财计划
 * @see TableLocation
 * @since 2017.11.03
 */
public class TableElement implements Comparable<TableElement>, ElementInterface {

    private int row;

    private int col;

    private String propertyName;

    private String fieldTitle;

    private Object value;

    @Override
    public String toHtml(int maxCols) {
        return toHtml(false, 0, maxCols);
    }

    public String toHtml(boolean isLastElement, int printedCols, int maxCols) {
        StringBuilder sb = new StringBuilder();
        int curPrintedCols = 0;
        if (StringUtils.isNotBlank(fieldTitle)) {
            sb.append("<td style=\"color:red\" align=\"center\" colspan='").append(maxCols)
                    .append("'><strong>～～～～～～").append(fieldTitle).append("～～～～～～</strong></td></tr><tr>");
        }
        if (StringUtils.isNotBlank(propertyName)) {
            sb.append("<td>").append(propertyName).append("</td>");
            curPrintedCols++;
        }

        if (value instanceof List) {
            List l = (List) value;
            for (int i = 0; i < l.size(); i++) {
                Object o = l.get(i);
                if (i == 0) {
                    sb.append("<td colspan='").append(maxCols).append("'>").append(o == null ? "" : o.toString()).append("</td></tr>");
                } else if (i == l.size() - 1) {
                    sb.append("<tr><td colspan='").append(maxCols).append("'>").append(o == null ? "" : o.toString()).append("</td>");
                } else {
                    sb.append("<tr><td  colspan='").append(maxCols).append("'>").append(o == null ? "" : o.toString()).append("</td></tr>");
                }
            }
        } else {
            sb.append("<td style=\"color:blue\" ");
            if (isLastElement) {
                int colspan = maxCols - printedCols - curPrintedCols;
                sb.append(" colspan=\"").append(colspan).append("\" ");
            }
            sb.append(">").append(value == null ? "" : value.toString()).append("</td>");
        }
        return sb.toString();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getCols() {
        int cols = 1;
        if (StringUtils.isNotBlank(propertyName)) {
            cols++;
        }
        return cols;
    }

    /**
     * 当前元素占了几行
     */
    public int getRows() {
        int rows = 1;
        if (StringUtils.isNotBlank(fieldTitle)) {
            rows++;
        }
        return rows;
    }


    @Override
    public int compareTo(TableElement o) {
        int r;
        return (r = row - o.row) != 0 ? r : col - o.col;
    }
}
