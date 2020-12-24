package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.Header;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;

/**
 * LengthFieldBasedFrameDecoder（基于长度字段的帧的解码器）:
 * 一个解码器，它根据消息中长度字段的值动态地分割接收到的{@link ByteBuf}。
 * 它是特别有用的，当你解码一个二进制消息，其中有一个整数头字段表示消息体或整个消息的长度。
 */
public class PrivateMsgDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * jboss 的 MarshallingDecoder
     */
    private MyMarshallingDecoder marshallingDecoder;

    /**
     * @param maxFrameLength 单条消息帧的最大长度，超过这个值，会报TooLongFrameException异常
     * @param lengthFieldOffset 指定字节长度的偏移量
     * @param lengthFieldLength 消息字段的长度
     */
    public PrivateMsgDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MyMarshallingDecoder();
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 调用父类的解码方法，返回整包或者半包消息（null）
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        // 返回null说明读取的是半包消息
        if (frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            HashMap<String, Object> attch = new HashMap<>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;

            for (int i=0; i < size; i++) {
                // 读key
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                // 读消息体，配置消息体
                attch.put(key, marshallingDecoder.decode(frame));
            }

            keyArray = null;
            key = null;

            header.setAttachment(attch);
        }

        if (frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }

        message.setHeader(header);

        return message;
    }
}
