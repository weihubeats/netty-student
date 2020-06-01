package client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import message.NettyMessage;

/**
 * @author WH
 * @version 1.0
 * @date 2020/6/1 21:30
 * @Description TODO
 */
@Slf4j
public class ClientHandler  extends ChannelHandlerAdapter {

    // 连接成功监听
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            NettyMessage message = (NettyMessage)msg;
            System.err.println("Client receive message from server: " + message.getBody());

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    // 客户端断开连接监听
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.info("----------客户端断开连接-----------");
        ctx.close();
    }

}
