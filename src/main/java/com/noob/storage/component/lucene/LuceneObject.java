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
    public Document asDocument();

    /**
     * 获取当前对象存储到Lucene中的ID<br/>
     * 返回结果不允许为空
     */
    public String getLuceneId();

    /**
     * 返回Lucene存储的数据列名集合
     */
    public String[] getLuceneFields();

    /**
     * 将一个Lucene查询出的Document对象转换为实体类
     */
    public LuceneObject getInstance(Document doc);
}
