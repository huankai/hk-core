package com.hk.authentication.rabbit.advice;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 消费者消费方法增强
 *
 * @author kevin
 * @date 2019-7-31 16:31
 */
public class RabbitSecurityContextMethodBeforeAdvice implements MethodBeforeAdvice {

    /**
     * before 代理的方法为 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     *
     * @param method 方法名 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     * @param args   方法参数
     * @param target {@link org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer}
     *               {@link org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer}
     */
    @Override
    public void before(Method method, Object[] args, Object target) {
     }
}
