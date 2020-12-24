package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * TODO 反正这个marshalling编解码的使用看不太懂，有空再去研究吧
 */
@ChannelHandler.Sharable
public class MyMarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    private Marshaller marshaller;

    public MyMarshallingEncoder() throws IOException {
        this.marshaller = MyMarshallingCodecFactory.buildMarshalling();;
    }

    protected void encode(Object msg, ByteBuf out) throws IOException {
        try {
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutPut output = new ChannelBufferByteOutPut(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos, out.writerIndex() - lengthPos -4);

        } catch (IOException e) {
            throw e;
        } finally {
            marshaller.close();
        }


    }
}
