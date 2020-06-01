package server;

import codec.NettyMessageDecoder;
import codec.NettyMessageEncoder;
import constant.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 15:35
 * @Description TODO
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }

    public void bind() throws Exception {
        //1 用于接受客户端连接的线程工作组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 用于对接受客户端连接读写操作的线程工作组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3 辅助类。用于帮助我们创建NETTY服务
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)//绑定两个工作线程组
                .channel(NioServerSocketChannel.class) //设置NIO的模式
                .option(ChannelOption.SO_BACKLOG, 1024) //设置TCP缓冲区
                .handler(new LoggingHandler(LogLevel.INFO))
                // 初始化绑定服务通道
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws IOException {
                        ch.pipeline().addLast(
                                new NettyMessageDecoder(1024 * 1024, 4, 4));
                        ch.pipeline().addLast(new NettyMessageEncoder());
                        ch.pipeline().addLast("readTimeoutHandler",
                                new ReadTimeoutHandler(50));
                        ch.pipeline().addLast(new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatHandler",
                                new HeartBeatRespHandler());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        // 绑定端口，同步等待成功
        ChannelFuture cf = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        log.info("Netty server start ok : {} : {}",NettyConstant.REMOTEIP, NettyConstant.PORT);

        //释放连接
        cf.channel().closeFuture().sync();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }


}
