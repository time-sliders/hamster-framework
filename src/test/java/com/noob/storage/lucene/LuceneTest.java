package com.noob.storage.lucene;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.noob.storage.component.lucene.LuceneComponent;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luyun
 * @since 2016.06.14
 */
public class LuceneTest {

    private LuceneComponent component = new LuceneComponent();

    @Test
    public void testAdd() {
        User user = new User();
        user.setName("鹏华中国50");
        user.setCode("160605");
        user.setSpell("phzg50");
        System.out.println("user保存结果:" + component.add(user) + "\t" + user);
    }

    @Test
    public void testQuery() throws Exception {
        List<User> userList = component.search("鹏国",
                new String[]{"name", "spell", "code"}, new UserLuceneConverter(), 500);
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(System.out::println);
        } else {
            System.out.println("无匹配结果!");
        }
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setName("鹏华中国50");
        user.setCode("160605");
        user.setSpell("newspell4");
        System.out.println("user 更新结果:" + component.update(user) + "\t" + user);
        try {
            testQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        System.out.println("user 删除结果:" + component.delete("160605"));
        try {
            testQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中文切词
     */
    @SuppressWarnings("unchecked")
    public static List<String> analyzerCnStr(String str){
        List<String> result = new ArrayList();
        Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47,true);//指定当前版本为4.6
        try {
            TokenStream tokenStream = analyzer.tokenStream("field", str);
            CharTermAttribute term=tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while( tokenStream.incrementToken() ){
                result.add( term.toString() );
            }
            tokenStream.end();
            tokenStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    @Test
    public void testAnalyzer() {
        List<String> l0 = analyzerCnStr("鹏华中国50");
        System.out.println(l0);

        List<String> l1 = analyzerCnStr("160605");
        System.out.println(l1);

        List<String> l2 = analyzerCnStr("phzg50");
        System.out.println(l2);
    }


    /**
     * 数字:605 -> *6*0*5*
     * 汉子不做处理
     */
    private String decorateQueryStr(String queryStr) {

        if (StringUtils.isBlank(queryStr)) {
            return null;
        }

        //去除用户输入的通配符
        queryStr = queryStr.replaceAll("[\\*\\?]+", "");

        StringBuilder sb = new StringBuilder("*");
        for (int i = 0; i < queryStr.length(); i++) {
            sb.append(queryStr.charAt(i)).append("*");
        }
        return sb.toString();
    }

}
