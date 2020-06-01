package server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import message.Header;
import message.MessageType;
import message.NettyMessage;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 15:31
 * @Description TODO
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 返回心跳应答消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_REQ
                .value()) {
            log.info("Receive client heart beat message : ---> {}", message);

            NettyMessage heartBeat = buildHeatBeat();
            log.info("Send heart beat response message to client : ---> {}", heartBeat);

            ctx.writeAndFlush(heartBeat);
        } else
            ctx.fireChannelRead(msg);
    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
