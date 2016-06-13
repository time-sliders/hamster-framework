package com.noob.storage.component.lucene;

import org.apache.commons.io.IOUtils;
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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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

    private static final String FS_DIRECTORY_PATH = "~/tmp/";

    /**
     * 获取文件系统的索引写入器
     *
     * @throws IOException
     */
    private IndexWriter getFSIndexWriter() throws IOException {
        Directory dir = FSDirectory.open(new File(FS_DIRECTORY_PATH));
        if (IndexWriter.isLocked(dir)) {
            IndexWriter.unlock(dir);
        }
        Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        return new IndexWriter(dir, config);
    }

    /**
     * 获取内存的索引写入器
     *
     * @throws IOException
     */
    private IndexWriter getRAMIndexWriter() throws IOException {
        Directory dir = new RAMDirectory();
        Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        return new IndexWriter(dir, config);
    }

    /**
     * 将指定的lucene对象添加到lucene索引中
     */
    public boolean add(LuceneObject luceneObject) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getRAMIndexWriter();
            Document doc = luceneObject.asDocument();
            indexWriter.addDocument(doc);
            indexWriter.commit();
            return true;
        } catch (IOException e) {
            logger.error("", e);
            return false;
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    public boolean update(LuceneObject luceneObject) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getRAMIndexWriter();
            Document doc = luceneObject.asDocument();
            Term term = new Term("id", luceneObject.getLuceneId());
            indexWriter.updateDocument(term, doc);
            indexWriter.commit();
            return true;
        } catch (IOException e) {
            logger.error("", e);
            return false;
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    public boolean delete(LuceneObject luceneObject) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getRAMIndexWriter();
            Term term = new Term("id", luceneObject.getLuceneId());
            indexWriter.deleteDocuments(term);
            return true;
        } catch (IOException e) {
            logger.error("", e);
            return false;
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    /**
     * 查询 从lucene索引引擎中检索数据
     *
     * @param queryStr  查询字符串
     * @param fields    属性列表
     * @param converter 转换器,复制将检索出的结果转换为业务模型
     */
    public List search(String queryStr, String[] fields,
                       AbstractLuceneDocumentConverter<?> converter) throws Exception {

        Directory directory = null;
        DirectoryReader reader = null;
        List<Object> result = null;

        try {
            directory = new RAMDirectory();
            reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);

            //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
            QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_47, fields, analyzer);

            //QueryParser parser = new QueryParser(Version.LUCENE_47, "xxx", analyzer);
            Query query = parser.parse(queryStr);
            TopDocs topDocs = searcher.search(query, 1);
            ScoreDoc[] hits = topDocs.scoreDocs;

            result = new LinkedList<>();
            for (ScoreDoc hit : hits) {
                Document hitDoc = searcher.doc(hit.doc);
                Object obj = converter.toDto(hitDoc);
                result.add(obj);
            }
        } catch (Exception e) {
            logger.error("", e);
            return result;
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(directory);
        }
        return result;
    }

}
