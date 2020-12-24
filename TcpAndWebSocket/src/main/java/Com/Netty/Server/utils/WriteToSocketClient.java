package Com.Netty.Server.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 向电机客户端写入数据
 */
public class WriteToSocketClient {


    private static Logger logger = LoggerFactory.getLogger(WriteToSocketClient.class);

    public static void writeToClient(final String receiveStr, ChannelGroup group, final String mark) {
        try {
            ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
            // ConvertCode.hexString2Bytes(receiveStr)
            // Hex.decodeHex(receiveStr.toCharArray())
            byte[] bytes = Hex.decodeHex(receiveStr.toCharArray());
            bufff.writeBytes(bytes);//对接需要16进制
           // System.out.println(Hex.decodeHex(receiveStr.toCharArray()));
            // group.writeAndFlush(bufff);
            group.forEach(channel -> {
                bufff.retain();     //https://www.cnblogs.com/lyzj/p/13207189.html
                channel.writeAndFlush(bufff)
                        .addListeners(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                StringBuilder sb = new StringBuilder("");
                                if(!StringUtils.isEmpty(mark)){
                                    sb.append("【").append(mark).append("】");
                                }
                                if (future.isSuccess()) {
                                    //System.out.println(sb.toString()+"回写成功"+receiveStr);
                                    logger.info(sb.toString()+"回写成功"+receiveStr);
                                } else {
                                    //System.out.println(sb.toString()+"回写失败"+receiveStr);
                                    logger.error(sb.toString()+"回写失败"+receiveStr);
                                }
                            }

                        });
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用通用writeToClient()异常"+e.getMessage());
            //log.error("调用通用writeToClient()异常：",e);
        }
    }
}
