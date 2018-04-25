package com.noob.storage.utils.print;


import com.alibaba.fastjson.JSON;
import com.noob.storage.model.JobResult;
import com.noob.storage.utils.print.element.Row;
import com.noob.storage.utils.print.element.Table;
import com.noob.storage.utils.print.element.TableElement;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 表格打印
 * <p>
 * 用于一些内部通知类邮件，采用一些html代码美化邮件样式。
 *
 * @author luyun
 * @version 理财计划
 * @since 2017.11.03
 */
public class TablePrinter {

    private static final Logger logger = LoggerFactory.getLogger(TablePrinter.class);

    /**
     * 构建Html信息
     */
    public static String printHtml(Object o) {

        try {
            //如果是JobResult对象 直接finish掉
            if (o instanceof JobResult) {
                JobResult object = (JobResult) o;
                object.finish();
            }
        } catch (Exception e) {
            logger.warn("", e);
        }

        try {

            /*
             * 1. 解析到数据模型
             */
            Table table = reflectToTableModel(o);
            if (table == null) {
                return null;
            }

            return table.toHtml(table.getCols());

        } catch (Throwable e) {
            logger.error("TablePrinter打印对象出现异常:" + JSON.toJSONString(o), e);
            return "TablePrinter打印对象出现异常,请查看日志并排查";
        }
    }

    private static Table reflectToTableModel(Object o) {

        /*
         * 获取所有添加TableLocation注解的属性
         * 并解析成TableElement
         */
        List<TableElement> elementList = initAllTableElement(o);
        if (CollectionUtils.isEmpty(elementList)) {
            return null;
        }

        /*
         * 将所有Element 根据row属性，包装成每一行
         */
        List<Row> rowList = buildRowList(elementList);

        Table table = new Table();
        table.setRowList(rowList);
        return table;
    }

    private static List<Row> buildRowList(List<TableElement> elementList) {

        Map<Integer, List<TableElement>> rowMap = new HashMap<Integer, List<TableElement>>();

        for (TableElement e : elementList) {
            List<TableElement> rowEleList = rowMap.get(e.getRow());
            if (rowEleList == null) {
                rowEleList = new LinkedList<TableElement>();
                rowMap.put(e.getRow(), rowEleList);
            }
            rowEleList.add(e);
        }

        List<Row> rowList = new LinkedList<Row>();
        for (Map.Entry<Integer, List<TableElement>> entry : rowMap.entrySet()) {
            Row row = new Row();
            row.setRow(entry.getKey());
            row.setElementList(entry.getValue());
            rowList.add(row);
        }

        return rowList;
    }

    private static List<TableElement> initAllTableElement(Object o) {

        List<Field> fields = getAnnoFields(o.getClass(), o);
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        List<TableElement> elementList = new LinkedList<TableElement>();
        for (Field field : fields) {
            TableLocation location = field.getAnnotation(TableLocation.class);
            if (location == null) {
                continue;
            }

            TableElement element = new TableElement();
            element.setRow(location.row());
            element.setCol(location.col());
            element.setFieldTitle(location.fieldTitle());
            element.setPropertyName(location.propertyName());
            try {
                field.setAccessible(true);
                element.setValue(field.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            elementList.add(element);
        }

        return elementList;
    }

    public static List<Field> getAnnoFields(Class clazz, Object o) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<Field> fieldList = new LinkedList<Field>();
        for (Field field : fields) {
            TableLocation location = field.getAnnotation(TableLocation.class);
            if (location == null) {
                continue;
            }
            fieldList.add(field);
        }

        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            List<Field> superClassFieldList = getAnnoFields(superClazz, o);
            if (CollectionUtils.isNotEmpty(superClassFieldList)) {
                fieldList.addAll(superClassFieldList);
            }
        }
        return fieldList;
    }


}
