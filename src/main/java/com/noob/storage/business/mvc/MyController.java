package com.noob.storage.business.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/say")
public class MyController {

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
}
