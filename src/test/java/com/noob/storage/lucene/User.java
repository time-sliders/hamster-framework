package com.noob.storage.lucene;

import com.noob.storage.component.lucene.LuceneObject;
import org.apache.lucene.document.*;

/**
 * @author luyun
 * @since 2016.06.14
 */
public class User implements LuceneObject {


    private String code;

    private String name;

    private String spell;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", spell='" + spell + '\'' +
                '}';
    }

    @Override
    public Document asDocument() {
        Document doc = new Document();
        doc.add(new Field("name", name, TextField.TYPE_STORED));
        doc.add(new Field("code", code, TextField.TYPE_STORED));
        doc.add(new Field("spell", spell, TextField.TYPE_STORED));
        doc.add(new Field("id",code,TextField.TYPE_STORED));

        return doc;
    }

    @Override
    public String getLuceneId() {
        return code;
    }
}
