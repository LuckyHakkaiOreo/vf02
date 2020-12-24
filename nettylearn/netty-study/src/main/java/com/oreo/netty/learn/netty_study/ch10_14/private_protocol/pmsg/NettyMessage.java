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
package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg;

import lombok.Data;

/**
 * 私有协议栈消息的唯一载体：NettyMessage
 * 它可以承载：握手请求/应答、心跳包等所有类型的消息
 * @author lilinfeng
 * @date 2014年3月14日
 * @version 1.0
 */
@Data
public final class NettyMessage {

    private Header header;

    private Object body;

}
