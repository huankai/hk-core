package com.hk.authentication.rabbit.advice;

import com.hk.authentication.headers.Header;
import com.hk.commons.util.JsonUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Stack;

/**
 * 消费者消费方法增强
 *
 * @author kevin
 * @date 2019-7-31 16:31
 * @see com.hk.authentication.interceptors.AuthenticationChannelInterceptor
 */
@Slf4j
public class RabbitSecurityContextMethodAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

    private final SecurityContext EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();

    private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();

    /**
     * 消费执行前: 将用户登陆信息放入上下文
     * before 增强的方法为 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     *
     * @param method 方法名 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     * @param args   方法参数
     * @param target 目标对象 {@link org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer}
     *               {@link org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer}
     */
    @Override
    public void before(Method method, Object[] args, Object target) {
        log.debug("before Advice,setSecurityContext...");
        Message message = (Message) args[1];
        Object authorization = message.getMessageProperties().getHeaders().get(Header.AUTHORIZATION_HEADER);
        if (Objects.nonNull(authorization)) {
            UserPrincipal userPrincipal = JsonUtils.deserialize(authorization.toString(), UserPrincipal.class);
            SecurityContext currentContext = SecurityContextHolder.getContext();
            Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
            if (contextStack == null) {
                contextStack = new Stack<>();
                ORIGINAL_CONTEXT.set(contextStack);
            }
            contextStack.push(currentContext);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            RabbitAuthenticationToken authenticationToken = new RabbitAuthenticationToken(userPrincipal, null);
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
        }
    }

    /**
     * 消费执行后: 将用户登陆信息从上下文清除
     * afterReturning 增强的方法为 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     *
     * @param method 方法名 {@link AbstractMessageListenerContainer#executeListener(Channel, Message)}
     * @param args   方法参数
     * @param target 目标对象 {@link org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer}
     *               {@link org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer}
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
        log.debug("afterReturning ,clean SecurityContext...");
        if (contextStack == null || contextStack.isEmpty()) {
            SecurityContextHolder.clearContext();
            ORIGINAL_CONTEXT.remove();
            return;
        }
        SecurityContext originalContext = contextStack.pop();
        try {
            if (EMPTY_CONTEXT.equals(originalContext)) {
                SecurityContextHolder.clearContext();
                ORIGINAL_CONTEXT.remove();
            } else {
                SecurityContextHolder.setContext(originalContext);
            }
        } catch (Throwable t) {
            SecurityContextHolder.clearContext();
        }
    }

    private static class RabbitAuthenticationToken extends AbstractAuthenticationToken {

        private UserPrincipal userPrincipal;

        private RabbitAuthenticationToken(UserPrincipal userPrincipal, Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            this.userPrincipal = userPrincipal;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return userPrincipal;
        }

        @Override
        public String getName() {
            return userPrincipal.getAccount();
        }
    }
}
