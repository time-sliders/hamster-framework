package com.noob.storage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LuYun
 * @version transfer split
 * @since 2018.08.20
 */
public class GenerateSQL {

    private static final String UPDATE_TRANSFER_PRODUCT_SQL
            = "update tz_transfer.transfer_product p,tz_transfer.transfer_product_change_idempotent i "
            + "set p.remain_amount = p.total_amount, p.status = 1, p.trade_num = 0 "
            + "where p.id = i.transfer_product_id and i.order_id = %s;";

    private static final String IN_ABSOLUTE_FILE_PATH
            = "/Users/zhangwei/Downloads/1";

    public static void main(String[] args) {

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            File file = new File(IN_ABSOLUTE_FILE_PATH);
            fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File outFile = new File("/Users/zhangwei/Downloads/" + dateStr + "_out.sql");
            if (outFile.exists() && !outFile.delete()) {
                throw new RuntimeException(outFile.getAbsolutePath() + " can not delete");
            }
            if (!outFile.createNewFile()) {
                throw new RuntimeException(outFile.getAbsolutePath() + " can not create new");
            }
            fos = new FileOutputStream(outFile);
            PrintWriter writer = new PrintWriter(fos);

            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                i++;

                // 处理一行数据
                writer.println(String.format(UPDATE_TRANSFER_PRODUCT_SQL, line));
                writer.flush();

                System.out.println("handle success > " + line);
            }
            System.out.println("total count: " + i);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }
}
