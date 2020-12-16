package com.itit.gateway.complete.aspect;

import com.itit.gateway.complete.filter.Filter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Response后置过滤处理切面
 *
 * @author zhangbotao
 */
@Aspect
@Component
public class ResponseFilterAspect {

    @Pointcut("@annotation(com.itit.gateway.complete.annotation.ResponseFilter)")
    public void clientExecute() {
    }

    @AfterReturning(value = "clientExecute()", returning = "result")
    public void responsetFilter(Object result) {
        FullHttpResponse response = (FullHttpResponse) result;
        Filter.responseProcess(response);
    }
}
