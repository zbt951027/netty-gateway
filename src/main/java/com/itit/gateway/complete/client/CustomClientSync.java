package com.itit.gateway.complete.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.itit.gateway.complete.annotation.RequestFilter;
import com.itit.gateway.complete.annotation.ResponseFilter;
import com.itit.gateway.complete.annotation.Route;
import com.itit.gateway.complete.common.HttpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自己实现的同步非阻塞客户端，性能勉强达到要求
 *
 * @author zhangbotao
 */
@Component
@EnableAspectJAutoProxy(exposeProxy = true)
public class CustomClientSync implements ClientSync, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(CustomClientSync.class);

    /**
     * 使用Map来保存用过的Channel，看下次相同的后台服务是否能够重用，起一个类似缓存的作用
     */
    private ConcurrentHashMap<Channel, Channel> channelPool = new ConcurrentHashMap<>();
    private EventLoopGroup clientGroup = new NioEventLoopGroup(new ThreadFactoryBuilder().setNameFormat("client work-%d").build());

    @Value("${client.SO_REUSEADDR}")
    private boolean soReuseaddr;
    @Value("${client.TCP_NODELAY}")
    private boolean tcpNodelay;
    @Value("${client.AUTO_CLOSE}")
    private boolean autoClose;
    @Value("${client.SO_KEEPALIVE}")
    private boolean soKeepalive;

    public CustomClientSync() {
    }

    /**
     * 调用channel发送请求，从handler中获取响应结果
     *
     * @param request       请求
     * @param serverChannel server outbound
     * @return 响应
     * @throws InterruptedException exception
     */
    private FullHttpResponse getResponse(FullHttpRequest request, Channel serverChannel) throws InterruptedException, URISyntaxException {
        // 查看缓存池中是否有可重用的channel
//        if (channelPool.containsKey(serverChannel)) {
//            Channel channel = channelPool.get(serverChannel);
//            if (!channel.isActive() || !channel.isWritable() || !channel.isOpen()) {
//                logger.debug("Channel can't reuse");
//            } else {
//                try {
//                    CustomClientSyncHandler handler = new CustomClientSyncHandler();
//                    channel.pipeline().replace("clientHandler", "clientHandler", handler);
//                    logger.info("请求体Request：{}", request);
//                    channel.writeAndFlush(request.retain()).sync();
//                    return handler.getResponse();
//                } catch (Exception e) {
//                    logger.debug("channel reuse send msg failed!");
//                    channel.close();
//                    channelPool.remove(serverChannel);
//                }
//                logger.debug("Handler is busy, please user new channel");
//            }
//        }


        // 没有或者不可用则新建
        // 并将最终的handler添加到pipeline中，拿到结果后返回
        CustomClientSyncHandler handler = new CustomClientSyncHandler();
        URI uri = new URI(request.uri());
        Channel channel = createChannel(uri.getHost(), uri.getPort());
        channel.pipeline().replace("clientHandler", "clientHandler", handler);
        channelPool.put(serverChannel, channel);
        logger.info("请求体Request：{}， Header信息：{}", request, HttpUtil.getRequestHeader(request));
        channel.writeAndFlush(request).sync();
        return handler.getResponse();
    }

    /**
     * 返回新的Channel
     *
     * @param address ip地址
     * @param port    端口
     * @return channel
     * @throws InterruptedException exception
     */
    private Channel createChannel(String address, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, soReuseaddr)
                .option(ChannelOption.TCP_NODELAY, tcpNodelay)
                .option(ChannelOption.AUTO_CLOSE, autoClose)
                .option(ChannelOption.SO_KEEPALIVE, soKeepalive)
                .handler(new CustomClientInitializer());
        return bootstrap.connect(address, port).sync().channel();
    }

    @Override
    @Route
    @RequestFilter
    @ResponseFilter
    public FullHttpResponse execute(FullHttpRequest request, Channel serverOutbound) {
        try {
            return getResponse(request, serverOutbound);
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭线程池
     */
    @Override
    public void destroy() {
        clientGroup.shutdownGracefully();
    }
}
