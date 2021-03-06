package com.oreo.netty.learn.netty_study.ch06.codec.serializer;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubscribeResp implements Serializable {
    /**
     * 默认序列ID
     */
    private static final long serialVersionUID = 1L;

    private int subReqID;

    private int respCode;

    private String desc;
}
