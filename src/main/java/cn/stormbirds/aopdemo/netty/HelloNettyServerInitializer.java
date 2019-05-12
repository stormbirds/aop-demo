package cn.stormbirds.aopdemo.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HelloNettyServerInitializer extends ChannelInitializer<SocketChannel> {
 
	//对chanel进行初始化
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		//通过socketChannel去获得对应的管道
		ChannelPipeline channelPipeline = socketChannel.pipeline();
		/**
		 * pipeline中会有很多handler类（也称之拦截器类）
		 * 获得pipeline之后，可以直接.add，添加不管是自己开发的handler还是netty提供的handler
		 * 
		 */
		//一般来讲添加到last即可，添加了一个handler并且取名为HttpServerCodec
		//当请求到达服务端，要做解码，响应到客户端做编码
		channelPipeline.addLast("HttpServerCodec", new HttpServerCodec());
		//添加自定义的CustomHandler这个handler，返回Hello Netty
		channelPipeline.addLast("customHandler", new CustomHandler());
		
	}
	
}