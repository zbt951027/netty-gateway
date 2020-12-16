package com.itit.gateway.complete.aspect;

import com.itit.gateway.complete.filter.Filter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Request前置过滤处理切面
 *
 * @author zhangbotao
 */
@Aspect
@Component
public class RequestFilterAspect {

    @Pointcut("@annotation(com.itit.gateway.complete.annotation.RequestFilter)")
    public void clientExecute() {
    }

    @Before("clientExecute()")
    public void requestFilter(JoinPoint point) {
        Object[] args = point.getArgs();
        FullHttpRequest request = (FullHttpRequest) args[0];
        Filter.requestProcess(request);
    }
}
