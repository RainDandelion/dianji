package com.ztguigu.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ztguigu.netty.proto.Me;

public class test {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        Me.SearchRequest build = Me.SearchRequest.newBuilder().setQuery("213").setCorpusValue(1).build();

        byte[] bytes = build.toByteArray();

        Me.SearchRequest searchRequest = Me.SearchRequest.parseFrom(bytes);
        System.out.println(searchRequest.getQuery());
        System.out.println(searchRequest.getCorpusValue());
    }
}
