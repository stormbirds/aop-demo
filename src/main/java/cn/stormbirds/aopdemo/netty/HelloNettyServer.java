package cn.stormbirds.aopdemo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloNettyServer {
	public static void main(String[] args) throws InterruptedException {
		/**
		 * 定义一对线程组（两个线程池）
		 * 
		 */
		//主线程组，用于接收客户端的链接，但不做任何处理
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//定义从线程组，主线程组会把任务转给从线程组进行处理
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			/**
			 * 服务启动类，任务分配自动处理
			 * 
			 */
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			//需要去针对一个之前的线程模型（上面定义的是主从线程）
			serverBootstrap.group(bossGroup, workerGroup)
				//设置NIO的双向通道
				.channel(NioServerSocketChannel.class)
				//子处理器，用于处理workerGroup
				/**
				 * 设置chanel初始化器
				 * 每一个chanel由多个handler共同组成管道(pipeline)
				 */
				.childHandler(new HelloNettyServerInitializer());	
			
			/**
			 * 启动
			 * 
			 */
			//绑定端口，并设置为同步方式，是一个异步的chanel
			ChannelFuture future = serverBootstrap.bind(8888).sync();
			
			/**
			 * 关闭
			 */
			//获取某个客户端所对应的chanel，关闭并设置同步方式
			future.channel().closeFuture().sync();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			//使用一种优雅的方式进行关闭
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
	}
}