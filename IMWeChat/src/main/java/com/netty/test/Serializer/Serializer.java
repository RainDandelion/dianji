package com.netty.test.Serializer;

public interface Serializer {

    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列号算法
     */
    byte getSerializerAlgorithm();
    /**
     * java对象转为二进制
     */
    byte[] serialize(Object o);


    /**
     * 二进制转为java对象
     */
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}
