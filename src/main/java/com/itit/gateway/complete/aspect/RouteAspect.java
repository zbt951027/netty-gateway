package com.itit.gateway.complete.aspect;

import com.itit.gateway.complete.route.RouteTable;
import io.netty.handler.codec.http.FullHttpRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 路由处理切面
 *
 * @author zhangbotao
 */
@Aspect
@Component
public class RouteAspect {

    @Pointcut("@annotation(com.itit.gateway.complete.annotation.Route)")
    public void clientExecute() {
    }

    @Before("clientExecute()")
    public void convert(JoinPoint point) {
        Object[] args = point.getArgs();
        FullHttpRequest request = (FullHttpRequest) args[0];
        request.setUri(RouteTable.getTargetUrl(request.uri()));
    }
}
