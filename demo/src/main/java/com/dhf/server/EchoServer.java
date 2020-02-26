package com.dhf.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        /*
        使用NIO接受和处理新的连接
        一个EventLoopGroup可以有多个EventLoop，每个EventLoop对应一个线程，所有EventLoop上的I/O事件都由
        该EventLoop对应的线程处理，一个Channel只能注册在一个EventLoop，一个EventLoop可以处理多个Channel，
        所以Channel不存在同步问题。
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 每个新连接都会创建一个channel，创建的channel会调用这里的方法进行定制，
                            // 这里为每个创建的channel都加一个ChannelHandle
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            ChannelFuture f = b.bind().sync(); // 异步绑定服务器，调用sync方法同步等待绑定完成
            System.out.println(EchoServer.class.getName() +
                    " started and listening for connections on " + f.channel().localAddress());
            f.channel().closeFuture().sync(); // 阻塞等待当前channel的close时间完成
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}