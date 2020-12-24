package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

/**
 * MessageToByteEncoder：从字面上应该能够理解，是将消息对象转为byte字节数组的编码器
 * 但是需要注意，netty都是面向缓冲区编程的，所以实际上是将消息对象转为ByteBuf的编码器
 */
public class PrivateMsgEncoder extends MessageToByteEncoder<NettyMessage> {

    MyMarshallingEncoder marshallingEncoder;


    public PrivateMsgEncoder() throws IOException {
        this.marshallingEncoder = new MyMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        // 要编码的消息不能为空
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode message is null");

        // 将消息转为二进制，写入头部数据
        out.writeInt(msg.getHeader().getCrcCode());
        out.writeInt(msg.getHeader().getLength());
        out.writeLong(msg.getHeader().getSessionID());
        out.writeByte(msg.getHeader().getType());
        out.writeByte(msg.getHeader().getPriority());
        out.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;

        for (Map.Entry<String, Object> param
                : msg.getHeader().getAttachment().entrySet()) {
            // 获取key的数组，写入key
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            out.writeInt(keyArray.length);
            out.writeBytes(keyArray);

            // 获取value的值，写入value（marshallingEncoder.encode）
            value = param.getValue();
            marshallingEncoder.encode(value, out);
        }

        key = null;
        keyArray = null;
        value = null;

        // 写消息体
        if (msg.getBody() != null)
            marshallingEncoder.encode(msg.getBody(), out);
        else
            out.writeInt(0);

        out.setInt(4, out.readableBytes() - 8);

    }
}
