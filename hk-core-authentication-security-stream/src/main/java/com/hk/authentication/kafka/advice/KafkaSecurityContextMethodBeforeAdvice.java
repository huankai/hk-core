package com.hk.authentication.kafka.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author kevin
 * @date 2019-7-31 17:11
 */
@Slf4j
public class KafkaSecurityContextMethodBeforeAdvice implements MethodBeforeAdvice {


    @Override
    public void before(Method method, Object[] args, Object target) {
        log.info(target.toString());
    }
}
