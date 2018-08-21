package com.noob.storage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * @author LuYun
 * @version transfer split
 * @since 2018.08.20
 */
public class GenerateSQL {

    private static final String UPDATE_TRANSFER_PRODUCT_SQL = "update tz_transfer.transfer_product p,tz_transfer.transfer_product_change_idempotent i set p.remain_amount = p.total_amount, p.status = 1, p.trade_num = 0 where  p.id = i.transfer_product_id and i.order_id = %s;";
    private static final String DELETE_IDEMPOTENT_SQL = "delete from tz_transfer.transfer_product_change_idempotent where order_id=%s;";
    private static final String DELETE_FLOW_SQL = "delete from tz_transfer.transfer_product_change_flow where order_id=%s;";

    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file = new File("/Users/zhangwei/Downloads/1");
            fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            File outFile = new File("/Users/zhangwei/Downloads/transfer_rollback_20180820.sql");
            if (outFile.exists() && !outFile.delete()) {
                throw new RuntimeException("can not delete");
            }
            if (!outFile.createNewFile()) {
                throw new RuntimeException("can not create new");
            }
            fos = new FileOutputStream(outFile);
            PrintWriter writer = new PrintWriter(fos);

            String linePreOrderId = null;
            int i = 1;
            while ((linePreOrderId = reader.readLine()) != null) {
                if (StringUtils.isBlank(linePreOrderId)) {
                    continue;
                }
                writer.println(String.format(UPDATE_TRANSFER_PRODUCT_SQL, linePreOrderId));
                writer.println(String.format(DELETE_IDEMPOTENT_SQL, linePreOrderId));
                writer.println(String.format(DELETE_FLOW_SQL, linePreOrderId));
                writer.flush();
                System.out.println(i++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }
}
