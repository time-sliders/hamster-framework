package com.noob.storage.utils.excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

/**
 * @author luyun
 * @since 2017.05.26
 */
public class ExcelReader {

    public static void main(String[] args) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("/Users/zhangwei/Downloads/1.xls"));
        Sheet sheet = workbook.getSheet(0);
        for (int i = 0; i < sheet.getRows(); i++) {//行
            for (int j = 0; j < 3; j++) {//列
                System.out.print(sheet.getCell(j, i).getContents() + "\t");
            }
            System.out.println();
        }
        workbook.close();
    }

}
