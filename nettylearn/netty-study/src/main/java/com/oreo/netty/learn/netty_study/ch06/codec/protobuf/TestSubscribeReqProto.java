/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oreo.netty.learn.netty_study.ch06.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @date 2014年2月23日
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq
                .newBuilder();
		builder.setSubReqId(1);
        builder.setUserName("Lilinfeng");
        builder.setProductName("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("NanJing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        builder.addAllAddress(address);
        return builder.build();
    }


    private static byte[] encodeResp(SubscribeRespProto.SubscribeResp resp) {
        return resp.toByteArray();
    }

    private static SubscribeRespProto.SubscribeResp decodeResp(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeRespProto.SubscribeResp.parseFrom(body);
    }
    private static SubscribeRespProto.SubscribeResp createSubscribeResp() {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp
                .newBuilder();
        builder.setSubReqID(1);
        builder.setDesc("Lilinfeng");
        builder.setRespCode(200);
        return builder.build();
    }

    /**
     * @param args
     * @throws InvalidProtocolBufferException
     */
    public static void main(String[] args)
            throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();

        System.out.println("Before encode : " + req.toString());

        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));

        System.out.println("After decode : " + req2.toString());

        System.out.println("Assert equal : --> " + req2.equals(req));
        System.out.println("-------------------------------------------------- ");

        SubscribeRespProto.SubscribeResp subscribeResp = createSubscribeResp();

        System.out.println("Before encode : " + subscribeResp.toString());

        SubscribeRespProto.SubscribeResp resp2 = decodeResp(encodeResp(subscribeResp));

        System.out.println("After decode : " + resp2.toString());

        System.out.println("Assert equal : --> " + resp2.equals(req));

    }

}
