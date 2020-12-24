package Com.Netty.Server.utils;

import Com.Netty.Server.handler.nettyWebServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PipelineAdd {

    public  void websocketAdd(ChannelHandlerContext ctx){
        System.out.println("PipelineAdd");
        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        ChannelPipeline channelPipeline = ctx.pipeline();
        channelPipeline.addBefore("commonhandler1","http-codec",new HttpServerCodec());
        channelPipeline.addBefore("commonhandler2","http-chunked",new ChunkedWriteHandler());
        channelPipeline.addBefore("commonhandler3","nettyWebServerHandler",new nettyWebServerHandler());
        channelPipeline.addBefore("commonhandler4","aggregator",new HttpObjectAggregator(1024 * 64));
        channelPipeline.addBefore("commonhandler5","LineBasedFrameDecoder",new LineBasedFrameDecoder(1024));  //解决tcp粘包问题
        channelPipeline.addBefore("commonhandler6","ProtocolHandler",new WebSocketServerProtocolHandler("/ws",true));
        channelPipeline.addBefore("commonhandler7","IdleStateHandler",new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));

    }
}