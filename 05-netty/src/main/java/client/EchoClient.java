package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import msgpack.MsgPackDecoder;
import msgpack.MsgPackEncoder;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:59
 * @Description TODO
 */
public class EchoClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 在MessagePack编码器之前添加 LengthFieldBasedFrameDecoder
                            // 它将在ByteBuf之前添加2个字节的消息长度字段
                           ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,
                                    2,0,2));

                            ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder());
                            // 在MEssagePack解码器之前添加 LengthFieldPrepender，用于处理半包消息
                            ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder", new MsgPackEncoder());
                            ch.pipeline().addLast(new EchoClientHandler(10));


                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放NIO线程租
            group.shutdownGracefully();
        }
    }
}
