package cn.stormbirds.aopdemo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

//处理的请求是：客户端向服务端发起送数据，先把数据放在缓冲区，服务器端再从缓冲区读取，类似于[ 入栈, 入境 ]
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {//Http请求，所以使用HttpObject
 
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel注册");
		super.channelRegistered(ctx);
	}
 
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel注册");
		super.channelUnregistered(ctx);
	}
 
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel活跃状态");
		super.channelActive(ctx);
	}
 
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端与服务端断开连接之后");
		super.channelInactive(ctx);
	}
 
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel读取数据完毕");
		super.channelReadComplete(ctx);
	}
 
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("用户事件触发");
		super.userEventTriggered(ctx, evt);
	}
 
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel可写事件更改");
		super.channelWritabilityChanged(ctx);
	}
 
	@Override
	//channel发生异常，若不关闭，随着异常channel的逐渐增多，性能也就随之下降
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("捕获channel异常");
		super.exceptionCaught(ctx, cause);
	}
 
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("助手类添加");
		super.handlerAdded(ctx);
	}
 
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("助手类移除");
		super.handlerRemoved(ctx);
	}
 
	/**
	 * ChannelHandlerContext：上下文对象
	 * 
	 * 
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		//获取当前channel
		Channel currentChannel = ctx.channel();
		
		//判断msg是否为一个HttpRequest的请求类型
		if(msg instanceof HttpRequest) {
 
			//客户端远程地址
			System.out.println(currentChannel.remoteAddress());
			/**
			 * 
			 * 未加判断类型，控制台打印的远端地址如下：
			 * 
			    /0:0:0:0:0:0:0:1:5501
				/0:0:0:0:0:0:0:1:5501
				/0:0:0:0:0:0:0:1:5502
				/0:0:0:0:0:0:0:1:5502
				/0:0:0:0:0:0:0:1:5503
				/0:0:0:0:0:0:0:1:5503
			 * 
			 * 原因是接收的MSG没有进行类型判断
			 * 
			 * 
			 * 增加了判断，为何还会打印两次？
			 * 
				/0:0:0:0:0:0:0:1:5605
				/0:0:0:0:0:0:0:1:5605
			 * 
			 * 打开浏览器的network会发现，客户端对服务端进行了两次请求：
			 * 	1、第一次是所需的
			 *  2、第二次是一个icon
			 *  因为没有加路由（相当于Springmvc中的requestMapping），只要发起请求，就都会到handler中去
			 * 
			 */
			/**
			 * 在Linux中也可以通过CURL 本机Ip:端口号 发送请求（只打印一次，干净的请求）			
			 */
			
			//定义发送的消息（不是直接发送，而是要把数据拷贝到缓冲区，通过缓冲区）
			//Unpooed：是一个专门用于拷贝Buffer的深拷贝，可以有一个或多个
			//CharsetUtil.UTF_8：Netty提供
			ByteBuf content = Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8);
			
			//构建一个HttpResponse，响应客户端
			FullHttpResponse response =
					/**
					 * params1:针对Http的版本号
					 * params2:状态（响应成功或失败）
					 * params3:内容
					 */
					//HttpVersion.HTTP_1_1：默认开启keep-alive
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
			//设置当前内容长度、类型等
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			//readableBytes：可读长度
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
			
			//通过长下文对象，把响应刷到客户端
			ctx.writeAndFlush(response);
			
		}
		
	}
 
}