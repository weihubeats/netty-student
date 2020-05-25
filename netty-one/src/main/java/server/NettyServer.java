package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/24 21:18
 * @Description netty 服务器端
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        int port = 8080;
        // 创建链各个Reactor线程租，一个用于服务端接受客户端连接
        // 一个用于SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // netty 用于启动NIO服务端的辅助启动类，不低降低服务端开发难度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)//配置NioServerSocketChannel 的TCP 参数
                    .childHandler(new ChildChannelHeandler());// 绑定I/O事件处理类 ChildChannelHeandler
            log.info("服务器启动" + "端口 {}", port);
            // 绑定端口，调用同步阻塞等待成功
            ChannelFuture f = b.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

}

class ChildChannelHeandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new TimeServerHandler());
    }
}




