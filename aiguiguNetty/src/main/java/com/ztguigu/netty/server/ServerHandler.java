package com.ztguigu.netty.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {


    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);  //充当业务线程池，可以将任务提交到该线程池中
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("handler线程 = " + Thread.currentThread().getName());

//        System.out.println("服务器读取线程" + Thread.currentThread().getName());
//        System.out.println("server cex = " + ctx);
//        System.out.println("channel 和 pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();   //双向链表
//
//        System.out.println(channel);
//        System.out.println(pipeline.channel());
//        ByteBuf byteBuf = (ByteBuf)msg;
//
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//
//        byteBuf.readBytes(bytes);
//
//
//        String temp = new String(bytes);
//        System.out.println("客户端打算消息：" + temp);
//        System.out.println("客户端地址：" + ctx.channel().remoteAddress());;


        /**
         * 时间耗时的业务  --》异步任务  --》
         * 解决方案1.
         *提交该channel ，对应NioEventloop的taskQueue中
         */
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                Thread.sleep(5 * 1000);
//                System.out.println("execute = " + Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端2，",CharsetUtil.UTF_8));
//        });
        /**
         * 解决方案2
         * 把任务提交到scheduleTackQueue中
         */
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端2，",CharsetUtil.UTF_8));
//            }
//        },5, TimeUnit.SECONDS);
//
//


        /**
         * 将任务提交到线程池中
         */
//        group.submit(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//
//                //接收客户端消息
//                ByteBuf byteBuf = (ByteBuf) msg;
//                byte[] bytes = new byte[byteBuf.readableBytes()];
//                byteBuf.readBytes(byteBuf);
//
//                String string = new String(bytes,"UTF-8");
//
//                Thread.sleep(5 * 1000);
//                System.out.println("group线程 = " + Thread.currentThread().getName());
//
//
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端，",CharsetUtil.UTF_8));
//                return null;
//            }
//        });

        System.out.println("go on....");
    }


    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端1", CharsetUtil.UTF_8));

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
        ctx.channel().close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
