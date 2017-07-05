package com.codingdie.nettytest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by xupen on 2017/7/5.
 */
public class TestClient {
    public static void main(String[] args) throws  Exception{
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int connectionNum = Integer.parseInt(args[2]);
        for(int i=0;i<connectionNum;i++){
            newConnection(host, port);
        }

    }

    public static void newConnection(String host, int port)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EventLoopGroup workerGroup = new NioEventLoopGroup();

                    Bootstrap b = new Bootstrap(); // (1)
                    b.group(workerGroup); // (2)
                    b.channel(NioSocketChannel.class); // (3)
                    b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
                    b.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addFirst(new IdleStateHandler(30,30,60));
                        }
                    });
                    // Start the client.
                    ChannelFuture f = b.connect(host, port).sync(); // (5)
                    // Wait until the connection is closed.
                    f.channel().closeFuture().sync();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }).start();

    }
}
