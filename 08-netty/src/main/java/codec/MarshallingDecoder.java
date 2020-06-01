package codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * @author WH
 * @version 1.0
 * @date 2020/5/31 14:26
 * @Description TODO
 */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;


    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
        //1 首先读取4个长度(实际body内容长度)
        int objectSize = in.readInt();
        //2 获取实际body的缓冲内容
        int readIndex = in.readerIndex();
        ByteBuf buf = in.slice(readIndex, objectSize);
        //3 转换
        ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
        try {
            //4 读取操作:
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            //5 读取完毕以后, 更新当前读取起始位置:
            //因为使用slice方法，原buf的位置还在readIndex上，故需要将位置重新设置一下
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } finally {
            unmarshaller.close();
        }
    }
}
