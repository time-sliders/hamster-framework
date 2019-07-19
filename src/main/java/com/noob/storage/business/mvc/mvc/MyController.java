package com.noob.storage.business.mvc.mvc;

import com.noob.storage.business.mvc.spring.MyComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/say")
public class MyController implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public MyController() {
        System.out.println(this.getClass().getSimpleName() + " Initial success");
    }

    @ResponseBody
    @RequestMapping(path = "/hello")
    public String sayHello(HttpServletRequest request,
                           HttpServletResponse response) {
        System.out.println("hello mvc");
        return "hello, spring mvc";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(this.getClass().getSimpleName() + " > " + applicationContext.getId());
        this.applicationContext = applicationContext;
    }
}
