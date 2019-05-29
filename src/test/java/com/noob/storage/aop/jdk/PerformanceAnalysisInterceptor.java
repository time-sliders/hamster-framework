package com.noob.storage.aop.jdk;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 卢云(luyun)
 * @version TPro
 * @since 2019.05.20
 */
@Component
@Aspect
public class PerformanceAnalysisInterceptor {

    private static Logger logger = LoggerFactory.getLogger(PerformanceAnalysisInterceptor.class);
    private static final long DELAY_MINUTE = 1000;

    @Around("execution (*  com.noob.storage.aop.jdk.BusinessServiceImpl.*(..))")
    public Object analysisAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        long startTime = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        long diffTime = endTime - startTime;

        logger.info("-----" + methodName + "执行时间 ：" + diffTime + " ms");
        if (diffTime > DELAY_MINUTE)
            delayWarning(methodName, diffTime - DELAY_MINUTE);

        return obj;
    }

    private void delayWarning(String methodName, long delayTime) {
        logger.warn("-----" + methodName + "超时 ：" + delayTime + " ms");
    }
}