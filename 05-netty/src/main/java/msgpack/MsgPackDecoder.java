package msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/28 23:52
 * @Description msgpack编码器
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        // 获取数据包 byteBuf中的byte数组
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack msgpack = new MessagePack();
        //调用 msgpack.read 反序列化然后加入到list中
        list.add(msgpack.read(array));

    }
}
