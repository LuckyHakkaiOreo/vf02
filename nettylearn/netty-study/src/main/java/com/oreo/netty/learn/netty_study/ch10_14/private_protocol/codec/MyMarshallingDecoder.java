package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

public class MyMarshallingDecoder {
    private final Unmarshaller unmarshaller;

    public MyMarshallingDecoder() throws IOException {
        unmarshaller = MyMarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws IOException, ClassNotFoundException {
        try {
            int objectSize = in.readInt();
            ByteBuf buf = in.slice(in.readerIndex(), objectSize);

            ByteInput input = new ChannelBufferByteInput(buf);
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }finally {
            unmarshaller.close();
        }

    }
}
