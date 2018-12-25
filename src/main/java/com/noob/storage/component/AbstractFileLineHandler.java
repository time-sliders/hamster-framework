package com.noob.storage.component;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;

public abstract class AbstractFileLineHandler {

    protected abstract String getInputFileAbsolutePath();

    protected abstract String getOutputFileAbsolutePath();

    protected abstract String handleLineData(String line);

    public void handle() {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            File file = new File(getInputFileAbsolutePath());
            fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            File outFile = new File(getOutputFileAbsolutePath());
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
                String lineDataHandleResult = handleLineData(line);
                if (StringUtils.isNotBlank(lineDataHandleResult)) {
                    writer.println(lineDataHandleResult);
                    writer.flush();
                    System.out.println("handle success > " + line);
                }
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
