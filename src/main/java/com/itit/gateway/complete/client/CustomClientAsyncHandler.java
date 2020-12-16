package com.itit.gateway.complete.client;

import com.itit.gateway.complete.common.CreateResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * 这里使用并发的等待-通知机制来拿到结果
 *
 * @author zhangbotao
 */
public class CustomClientAsyncHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(CustomClientAsyncHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpResponse fullHttpResponse) throws Exception {
        ByteBuf content = fullHttpResponse.content();
        HttpHeaders headers = fullHttpResponse.headers();

        logger.info("content:" + System.getProperty("line.separator") + content.toString(CharsetUtil.UTF_8));
        logger.info("headers:" + System.getProperty("line.separator") + headers.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI url = new URI("/test");
        String meg = "hello";

        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.GET, url.toASCIIString(), Unpooled.wrappedBuffer(meg.getBytes("UTF-8")));

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //发送数据
        ctx.writeAndFlush(request);
    }
}
