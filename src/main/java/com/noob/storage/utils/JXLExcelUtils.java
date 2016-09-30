package com.noob.storage.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luyun
 */
public class JXLExcelUtils {

    public static void write(File excelFile, int sheetIndex, int row, int col, String data)
            throws IOException, WriteException {

        WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
        write(workbook, sheetIndex, row, col, data);
        workbook.write();
        workbook.close();
    }

    /**
     * 写入数据到Excel
     *
     * @param workbook excel文件
     * @param row      行
     * @param col      列
     * @param data     写入的数据
     */
    public static void write(WritableWorkbook workbook, int row, int col, String data)
            throws IOException, WriteException {
        write(workbook, 1, row, col, data);
    }

    /**
     * 写入数据到Excel
     *
     * @param workbook   excel文件
     * @param sheetIndex 标签页 下标
     * @param row        行
     * @param col        列
     * @param data       写入的数据
     */
    public static void write(WritableWorkbook workbook, int sheetIndex, int row, int col, String data)
            throws IOException, WriteException {

        WritableSheet sheet = workbook.getSheet(sheetIndex);
        Label label = new Label(col, row, data);
        sheet.addCell(label);
    }


    public static void main(String[] args) throws Exception {
        File fund = new File("/Users/zhangwei/Downloads/fund");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fund)));
        String fundCode;
        Set<String> fundCodeSet = new HashSet<String>();
        while ((fundCode = reader.readLine()) != null) {
            fundCodeSet.add(fundCode);
        }

        File configFile = new File("/Users/zhangwei/Downloads/day");
        FileInputStream fis = new FileInputStream(configFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(fis, bos);
        String s = bos.toString();
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(bos);
        String excelUrl = "/Users/zhangwei/Downloads/1.xls";
        String[] dataArr = s.replaceAll("\\[", "").replaceAll("\"", "").split("\\],", -1);
        WritableWorkbook wwb = Workbook.createWorkbook(new File(excelUrl));
        WritableSheet ws = wwb.createSheet("test", 0);
        int row = 0;
        for (String rowData : dataArr) {
            String[] rowArr = rowData.split(",", -1);
            System.out.println(rowData);
            String rowFundCode = rowArr[0];
            if (!fundCodeSet.contains(rowFundCode)) {
                continue;
            }
            Label label1 = new Label(0, row, rowArr[0]);
            Label label2 = null;
            if ("月结".equals(rowArr[7])) {
                label2 = new Label(1, row, "1");
            } else if ("日结".equals(rowArr[7])) {
                label2 = new Label(1, row, "2");
            }
            Label label3 = new Label(2, row, rowArr[8]);
            ws.addCell(label1);
            ws.addCell(label2);
            ws.addCell(label3);
            row++;
        }
        wwb.write();
        wwb.close();
    }

}