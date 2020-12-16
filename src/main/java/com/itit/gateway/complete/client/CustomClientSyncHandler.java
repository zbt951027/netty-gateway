package com.itit.gateway.complete.client;

import com.itit.gateway.complete.common.CreateResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 这里使用并发的等待-通知机制来拿到结果
 *
 * @author zhangbotao
 */
public class CustomClientSyncHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(CustomClientSyncHandler.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private FullHttpResponse response;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        // 拿到结果后再释放锁
        logger.info(Thread.currentThread().getName() + " 已拿到结果: " + msg);
        response = CreateResponse.createResponse(msg);
        latch.countDown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 阻塞等待结果后返回
     *
     * @return 后台服务器响应
     * @throws InterruptedException exception
     */
    public FullHttpResponse getResponse() throws InterruptedException {
        logger.info(Thread.currentThread().getName() + " 等待返回结果");
        latch.await();
        logger.info(Thread.currentThread().getName() + " 已返回结果: " + response);
        return response;
    }
}
