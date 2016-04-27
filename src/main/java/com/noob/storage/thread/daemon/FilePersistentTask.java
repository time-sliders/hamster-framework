package com.noob.storage.thread.daemon;

import com.noob.storage.component.FilePersistentComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * 文件持久任务
 * <p>主要是为了防止宕机造成数据错误，该任务在执行之前，会在本地生成一个临时文件。执行成功会删除该文件，
 * 系统启动的时候会自动加载本地临时文件到内存并执行
 */
public abstract class FilePersistentTask extends DaemonThread implements Serializable {

    private static Logger log = Logger.getLogger(FilePersistentTask.class);

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_TEXT = "0";

    /**
     * 该对象在本地的文件持久
     */
    private String tempFileUrl;

    public FilePersistentTask() {
        super();
        tempFileUrl = FilePersistentTaskLoader.tempRootPath
                + this.getClass().getCanonicalName()
                + File.separatorChar + this.getTaskName();
    }

    @Override
    public void before() {
        try {
            FilePersistentComponent.writeStringToFile(FilePersistentTaskLoader.tempRootPath
                    + this.getClass().getCanonicalName()
                    + File.separatorChar, this.getTaskName(), DEFAULT_TEXT);
        } catch (IOException e) {
            log.error("文件持久任务创建临时文件失败", e);
        }
    }

    @Override
    public void afterSuccess() {
        File file = new File(tempFileUrl);
        FileUtils.deleteQuietly(file);
    }

}
