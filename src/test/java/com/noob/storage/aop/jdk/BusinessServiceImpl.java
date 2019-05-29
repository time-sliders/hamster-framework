package com.noob.storage.aop.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("businessService")
public class BusinessServiceImpl implements IBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Transactional
    public void executeBusinessA() throws InterruptedException {
        //模拟一个执行时间
        Thread.sleep(200);
        logger.info("executing business in methodA");
    }

}
