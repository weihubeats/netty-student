package msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:49
 * @Description MsgPackEncoder 继承 MessageToByteEncoder
 * 它负责将Object类型的POJO对象编码为byte数组，然后写入到ByteBuf中
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack = new MessagePack();
        //Serialize
        byte[] raw = msgpack.write(o);
        byteBuf.writeBytes(raw);
    }


}
