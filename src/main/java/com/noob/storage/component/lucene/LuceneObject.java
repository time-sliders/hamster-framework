package com.noob.storage.component.lucene;

import org.apache.lucene.document.Document;

/**
 * 一个可以添加到Lucene索引引擎中的对象
 *
 * @author luyun
 * @since 2016.06.13
 */
public interface LuceneObject {

    /**
     * 将当前对象转换为lucene可存储的Document
     */
    Document asDocument();

    /**
     * 获取当前对象的Lucene ID[唯一]
     * <br/>
     * lucene更新&删除数据时需要用到
     */
    String getLuceneId();
}
