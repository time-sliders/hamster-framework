package com.noob.storage.lucene;

import com.noob.storage.component.lucene.AbstractLuceneDocumentConverter;
import org.apache.lucene.document.Document;

/**
 * @author luyun
 * @since 2016.06.14
 */
public class UserLuceneConverter extends AbstractLuceneDocumentConverter<User> {

    @Override
    protected User onBuildDto(Document model) {

        if (model == null) {
            return null;
        }

        User user = new User();
        user.setCode(model.get("code"));
        user.setName(model.get("name"));
        user.setSpell(model.get("spell"));

        return user;
    }

}
