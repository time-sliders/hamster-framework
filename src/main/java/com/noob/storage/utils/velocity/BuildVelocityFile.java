package com.noob.storage.utils.velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author luyun
 * @version APP 6.6 (Fund aip)
 * @since 2017.05.17
 */
public class BuildVelocityFile {

    public static void main(String[] args) {
        try {
            String table = "fund_portfolio";
            String beanName = "FundPortfolio";
            String ip = "192.168.1.151";
            String port = "3306";
            String schema = "fund";
            String userName = "tbj";
            String password = "tbj900900";

            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + schema + "?useUnicode=true&characterEncoding=utf-8", userName, password);
            TableConfig tableConfig = getTable(connection, table);
            connection.close();
            tableConfig.setBeanName(beanName);
            tableConfig.setInjectName(StringUtils.getFistLowName(beanName));
            File file = new File("/Users/zhangwei/Downloads/test.zip");
            if (file.exists() && !file.delete()) {
                throw new RuntimeException("delete fail");
            }
            if (!file.createNewFile()) {
                throw new RuntimeException("create fail");
            }
            FileOutputStream fis = new FileOutputStream(new File("/Users/zhangwei/Downloads/test.zip"));
            CheckedOutputStream cos = new CheckedOutputStream(fis, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            printMapper(tableConfig, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMapper(TableConfig table, ZipOutputStream out) throws IOException {

        String buffer = TemplateBuilder.build(table, TemplateBuilder.MAPPER);
        printFile(buffer, table.getNamespace(), table.getTableName() + ".xml", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.MODEL);
        printFile(buffer, table.getNamespace(), table.getBeanName() + ".java", out);

        out.flush();
    }

    private static void printFile(String buffer, String pkg, String name, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        out.putNextEntry(entry);
        out.write(buffer.getBytes());
    }


    private static TableConfig getTable(Connection conn, String table) throws SQLException, UnsupportedEncodingException {
        TableConfig tableConfig = new TableConfig();
        DatabaseMetaData dbMetData = conn.getMetaData();
        List<ColumnConfig> columns = getColumns(dbMetData, table);
        String beanTypeName = StringUtils.getJavaName(table);
        tableConfig.setPackageName(beanTypeName);
        tableConfig.setBeanName(beanTypeName);
        tableConfig.setInjectName(StringUtils.getFistLowName(beanTypeName));
        tableConfig.setTableName(table);
        tableConfig.setNamespace(beanTypeName);
        tableConfig.setColumns(columns);
        if (columns != null) {
            for (ColumnConfig columnConfig : columns) {
                if (columnConfig.isPrimaryKey()) {
                    tableConfig.setPrimaryKey(columnConfig);
                    break;
                }
            }
        }
        return tableConfig;
    }

    private static List<ColumnConfig> getColumns(DatabaseMetaData metaData, String table) throws SQLException, UnsupportedEncodingException {
        List<ColumnConfig> columns = new LinkedList<ColumnConfig>();
        // 根据表名提前表里面信息：
        ResultSet primaryKeySet = metaData.getPrimaryKeys(null, null, table);
        String primaryKey = null;
        while (primaryKeySet.next()) {
            primaryKey = primaryKeySet.getString("COLUMN_NAME");
        }
        ResultSet colRet = metaData.getColumns(null, "%", table, "%");
        while (colRet.next()) {
            String columnName = colRet.getString("COLUMN_NAME");
            Integer columnType = colRet.getInt("DATA_TYPE");
            String typeName = colRet.getString("TYPE_NAME");
            String comment = colRet.getString("REMARKS");
            comment = new String(comment.getBytes(), "utf-8");
            boolean isPrimaryKey = false;
            if (columnName.equals(primaryKey)) {
                isPrimaryKey = true;
            }
            try {
                ColumnConfig columnConfig = buildConlumnConfig(columnName, columnType, isPrimaryKey);
                columnConfig.setComment(comment);
                columns.add(columnConfig);
            } catch (Exception e) {
                throw new RuntimeException("ConlumnConfig构建失败:" + table + " " + columnName + " " + columnType + "" + typeName);
            }
        }
        return columns;
    }

    private static ColumnConfig buildConlumnConfig(String columnName, Integer columnType, boolean isPrimaryKey) {
        ColumnConfig column = new ColumnConfig(isPrimaryKey, columnName, StringUtils.getFistLowName(StringUtils.getJavaName(columnName)));
        JdbcTypeService.JdbcType type = JdbcTypeService.getInstances().getJavaType(columnType);
        column.setColumnType(type.getJdbcType());
        column.setJavaType(type.getJavaType());
        column.setSimpleJavaType(StringUtils.getSimpleName(type.getJavaType()));
        return column;
    }
}
