package server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:09
 * @Description TODO
 */
@Slf4j
public class EchoServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(msg);
        log.info("服务端接受到的消息为：{}", msg);
    }
}
