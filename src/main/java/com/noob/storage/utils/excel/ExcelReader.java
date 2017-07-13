package com.noob.storage.utils.excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author luyun
 * @since 2017.05.26
 */
public class ExcelReader {

    private static final String DEFAULT_YEAR = "20";

    public static void main(String[] args) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("/Users/zhangwei/Downloads/5.xls"));

        File file = new File("/Users/zhangwei/Downloads/js.txt");
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("delete file fail");
        }
        if (!file.createNewFile()) {
            throw new RuntimeException("create file fail");
        }
        FileOutputStream fos = new FileOutputStream(file);

        Sheet sheet = workbook.getSheet(0);
        for (int i = 1; i < sheet.getRows(); i++) {//行

            StringBuilder sb = new StringBuilder();
            sb.append(sheet.getCell(0, i).getContents()).append("|");

            String dateStr = sheet.getCell(1, i).getContents();
            String[] dateStrArr = dateStr.split("-");
            String Year = DEFAULT_YEAR + dateStrArr[0];
            String Month = StringUtils.leftPad(dateStrArr[1], 2, "0");
            String DaY = StringUtils.leftPad(dateStrArr[2], 2, "0");
            sb.append(Year).append(Month).append(DaY).append("|");

            sb.append(new BigDecimal(sheet.getCell(3, i).getContents()).setScale(4, BigDecimal.ROUND_HALF_UP)).append("\n");
            System.out.print(sb.toString());
            fos.write(sb.toString().getBytes());
            fos.flush();
        }
        workbook.close();
        fos.close();
    }

}
