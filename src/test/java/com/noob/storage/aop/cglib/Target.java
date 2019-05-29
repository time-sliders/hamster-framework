package com.noob.storage.aop.cglib;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Target {

    public void A() {
        System.out.println("Target.A() invoked");
    }

    @Transactional
    public void B(){
        System.out.println("Target.B() invoked");
    }
}
