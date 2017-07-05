package com.codingdie.nettytest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
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
import org.junit.Test;

import java.util.concurrent.TimeUnit;


/**
 * Created by xupen on 2017/7/5.
 */


public class NettyServerTest {
    @Test
    public void testA(){
       new TestClient().newConnection("127.0.0.1",9000);
        try {
            Thread.sleep(100000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
