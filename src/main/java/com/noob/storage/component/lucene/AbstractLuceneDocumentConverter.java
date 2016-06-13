package com.noob.storage.component.lucene;

import com.noob.storage.common.converter.AbstractObjectConverter;
import org.apache.lucene.document.Document;

/**
 * Lucene 对象转换器<br/>
 * 子类实现该接口,将lucene的Doc对象转换为业务需要的对象
 *
 * @author luyun
 * @since 2016.06.13
 */
public abstract class AbstractLuceneDocumentConverter<E> extends AbstractObjectConverter<Document,E> {

}
