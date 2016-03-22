package com.noob.storage.thread.daemon;

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
        createTaskFile(this);
    }

    @Override
    public void afterSuccess() {
        File file = new File(tempFileUrl);
        deleteFile(file);
    }

    /**
     * 将守护任务落地持久
     */
    public void createTaskFile(DaemonThread task) {

        if (task == null || task.isTerminated()) {
            return;
        }

        File path = new File(FilePersistentTaskLoader.tempRootPath +
                task.getClass().getCanonicalName() + File.separatorChar);
        if (!path.exists() && !path.mkdirs()) {
            throw new RuntimeException("文件创建失败");
        }
        File file = new File(tempFileUrl);
        deleteFile(file);
        ObjectOutputStream oos = null;
        try {
            if (file.createNewFile()) {
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(task);
                oos.flush();
            } else {
                throw new RuntimeException("文件创建失败");
            }
        } catch (IOException e) {
            log.info("创建临时文件失败:" + e.getMessage(), e);
            deleteFile(file);
        } finally {
            IOUtils.closeQuietly(oos);
        }
    }

    /**
     * 删除任务在文件系统中的文件
     */
    public void deleteFile(File file) {
        if (file != null && file.exists() && !file.delete()) {
            log.error("删除文件{" + file.getName() + "}失败");
        }
    }

}
