package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import msgpack.MsgPackDecoder;
import msgpack.MsgPackEncoder;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:01
 * @Description FixedLengthFrameDecoder定长解码器
 */
public class EchoServer {

    public static void main(String[] args) {
        //配置服务端的NIO线程租
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,
                                    2,0,2));
                            ch.pipeline().addLast(new MsgPackDecoder());
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new MsgPackEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                            /*ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,
                                    2,0,2));
                            ch.pipeline().addLast(new MsgPackDecoder());
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new MsgPackEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());*/

                        }
                    });

            //绑定端口，成功同步等待
            ChannelFuture f = b.bind(8080).sync();
            //订单服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
