package com.noob.storage.component.lucene;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Lucene 索引引擎组件
 *
 * @author luyun
 * @since 2016.06.13
 */
public class LuceneComponent {

    private static final Logger logger = LoggerFactory.getLogger(LuceneComponent.class);

    // 默认数据文件的存放路径
    private static final String FS_DIRECTORY_PATH = "/Users/zhangwei/test/lucene/";

    private static final String ID = "id";

    private static final Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);

    private Directory dir = null;

    public LuceneComponent() {
        this(FS_DIRECTORY_PATH);
    }

    public LuceneComponent(String fsDirectoryPath) {
        try {
            this.dir = FSDirectory.open(new File(fsDirectoryPath));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加lucene索引<br/>
     * 为防止数据重复,这里直接调用update即可.
     */
    public boolean add(LuceneObject luceneObject) {
        return update(luceneObject);
    }

    /**
     * 更新lucene索引<br/>
     * lucene update 会先删除原有数据,再增添新数据.
     */
    public boolean update(LuceneObject luceneObject) {

        Assert.notNull(luceneObject, "luceneObject must not be null!");
        Assert.hasText(luceneObject.getLuceneId(), "luceneId must not be null!");

        IndexWriter indexWriter = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            indexWriter = new IndexWriter(dir, config);
            Document doc = luceneObject.asDocument();
            Term term = new Term(ID, luceneObject.getLuceneId());
            indexWriter.updateDocument(term, doc);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("", e);
            return false;
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    /**
     * 根据luceneId删除索引
     */
    public boolean delete(String luceneId) {

        Assert.hasText(luceneId, "luceneId must not be null!");

        IndexWriter indexWriter = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            indexWriter = new IndexWriter(dir, config);
            Term term = new Term(ID, luceneId);
            indexWriter.deleteDocuments(term);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("", e);
            return false;
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    /**
     * 检索&查询
     *
     * @param queryStr  输入的查询字符串
     * @param fields    查询的属性名称列表(尽可能的缩小查询范围)
     * @param converter 转换器,将检索出的结果转换为业务模型
     * @param limit     最大查询的数据量
     */
    public <E extends LuceneObject> List<E> search(String queryStr,
                                                   String[] fields,
                                                   AbstractLuceneDocumentConverter<E> converter,
                                                   int limit) throws Exception {

        Assert.notEmpty(fields, "fields must not be empty!");
        Assert.hasText(queryStr, "queryStr must not be null!");
        Assert.notNull(converter, "converter must not be null!");

        limit = limit <= 0 ? 1 : limit;

        DirectoryReader reader = null;
        List<E> l = null;

        try {
            reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_47, fields, analyzer);
            parser.setAllowLeadingWildcard(true); // 允许首字母为通配符.

            Query query = parser.parse(queryStr);
            TopDocs topDocs = searcher.search(query, limit);
            ScoreDoc[] hits = topDocs.scoreDocs;

            if (ArrayUtils.isEmpty(hits)) {
                return null;
            }

            l = new LinkedList<>();
            for (ScoreDoc hit : hits) {
                Document hitDoc = searcher.doc(hit.doc);
                E e = converter.toDto(hitDoc);
                l.add(e);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("", e);
            return l;
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return l;
    }


}
