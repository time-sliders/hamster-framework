package com.noob.storage.thread.daemon;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件持久任务加载器
 */
public class FilePersistentTaskLoader {

    private static Logger log = Logger.getLogger(FilePersistentTaskLoader.class);

    /**
     * 临时文件存放的目录
     */
    public static final String tempRootPath = System.getProperty("user.dir")
            + File.separatorChar + "DAEMON_TEMP_FOLDER" + File.separatorChar;

    /**
     * 加载临时目录下所有的临时文件
     */
    public static List<DaemonThread> loadAllTasks() {

        List<DaemonThread> tasks = new LinkedList<DaemonThread>();

        File rootFile = new File(tempRootPath);
        if (!rootFile.exists() && rootFile.mkdirs()) {
            return null;
        }

        File[] classDirs = rootFile.listFiles();
        if (ArrayUtils.isEmpty(classDirs)) {
            return null;
        }

        for (File classDir : classDirs) {
            if (!classDir.isDirectory()) {
                continue;
            }
            File[] taskFiles = classDir.listFiles();
            if (taskFiles == null || taskFiles.length <= 0) {
                continue;
            }
            for (File taskFile : taskFiles) {
                if (!taskFile.isFile()) {
                    continue;
                }
                DaemonThread daemonThread = loadTask(taskFile);
                if (daemonThread != null) {
                    tasks.add(daemonThread);
                }
            }
        }

        return tasks;
    }

    /**
     * 根据类名以及文件加载一个任务
     *
     * @param taskFile 该类的一个对象落地文件
     */
    private static DaemonThread loadTask(File taskFile) {

        if(taskFile == null){
            return null;
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(taskFile));
            DaemonThread task = (DaemonThread) ois.readObject();
            log.info("成功加载文件持久守护线任务[" + task + "]");
            return task;
        } catch (Throwable e) {
            log.error("加载文件持久守护任务失败[" + e.getMessage() + "]", e);
            return null;
        } finally {
            IOUtils.closeQuietly(ois);
        }
    }

}
