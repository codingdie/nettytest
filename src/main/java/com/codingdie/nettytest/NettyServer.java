package com.codingdie.nettytest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.util.concurrent.TimeUnit;


/**
 * Created by xupen on 2017/7/5.
 */
public class NettyServer {
    public static void main(String[] args) throws  Exception {
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
         ChannelGroup channels=new DefaultChannelGroup(new DefaultEventExecutor());

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                channels.add(ch);
            }
        }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

        ChannelFuture channelFuture = serverBootstrap.bind(Integer.valueOf(args[0])).sync();
        final Timer timer = new HashedWheelTimer();
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("当前可用连接数:"+channels.size());
                timer.newTimeout(timeout.task(),3L,TimeUnit.SECONDS);
            }
        },3L, TimeUnit.SECONDS);

        System.out.println("服务器已经启动");
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        });
    }
}
