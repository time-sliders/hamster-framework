package com.noob.storage.business.mvc.spring;

import com.noob.storage.business.mvc.mvc.MyController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.07.19
 */
@Component
public class MyComponent implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private MyController myController;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(this.getClass().getSimpleName() + " > " + applicationContext.getId());
        System.out.println(myController.getClass().toString());
        this.applicationContext = applicationContext;
    }
}
