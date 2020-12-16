package com.itit.gateway.complete.server;

import com.itit.gateway.complete.client.ClientSync;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 服务端配置启动
 *
 * @author zhangbotao
 */
@Component
@Order(2)
public class Server implements ApplicationRunner, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Autowired
    private ClientSync clientSync;

    private final
    Environment environment;

    private EventLoopGroup bossGroup;
    private EventLoopGroup serverGroup;

    @Value("${server.SO_REUSEADDR}")
    private boolean soReuseaddr;

    @Value("${server.AUTO_CLOSE}")
    private boolean autoClose;

    public Server(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int bossNumber = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.boos.threads")));
        int workNumber = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.work.threads")));

        bossGroup = new NioEventLoopGroup(bossNumber);
        serverGroup = new NioEventLoopGroup(workNumber);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, serverGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, soReuseaddr)
                .option(ChannelOption.AUTO_CLOSE, autoClose)
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 使用同步非阻塞方式
                .childHandler(new ServerSyncInitializer(clientSync));

        int port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.port")));
        Channel channel = serverBootstrap.bind(port).sync().channel();
        logger.info("SO_REUSEADDR::" + soReuseaddr + "  AUTO_CLOSE::" + autoClose);
        logger.info("Gateway lister on port: " + port);
        channel.closeFuture().sync();
    }

    @Override
    public void destroy() {
        bossGroup.shutdownGracefully();
        serverGroup.shutdownGracefully();
    }
}
