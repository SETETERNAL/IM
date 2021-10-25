package im.server;

import im.server.commpent.zookeeper.ZookeeperUtil;
import im.server.handler.*;
import im.util.PacketEncoderAndDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cch
 * @date 2021/6/17 10:43
 */
@Slf4j
public class ServerMain {

    public static Integer port = null;

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        EventLoopGroup work = new NioEventLoopGroup(0, new DefaultThreadFactory("work"));
        serverBootstrap.group(boss, work)
//                .option(ChannelOption.SO_BACKLOG, 100)
//                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch){
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new IMIdleStateHandler());
                        pipeline.addLast(PacketEncoderAndDecoder.INSTANCE);
                        pipeline.addLast(HeartBeatHandler.INSTANCE);
                        pipeline.addLast(LoginServerHandler.INSTANCE);
                        pipeline.addLast(AuthHandler.INSTANCE);
                        pipeline.addLast(ServerReceivedMessageHandler.INSTANCE);
                        pipeline.addLast(ErrorMessageHandler.INSTANCE);
                        pipeline.addLast(ExceptionCaughtHandler.INSTANCE);
                    }
                });
        ChannelFuture future = bind(serverBootstrap, 8080).sync();
        future.channel().closeFuture().sync();
    }

    /**
     * @author cch
     * @param serverBootstrap
     * @param port
     * @return
     * 这种动态绑定端口号的情况在一般情况用不上 <br/>
     * 但在微服务的情况下是需要的，即服务器绑定端口后向注册中心发送信息，而客户端在注册中心获取配置后访问服务器
     */
    private static ChannelFuture bind(io.netty.bootstrap.ServerBootstrap serverBootstrap, final int port){
        ChannelFuture future = serverBootstrap.bind(port).addListener(future1 -> {
            if(future1.isSuccess()){
                ServerMain.port = port;
                log.info("端口绑定:{}", port);
                // zookeeper服务
                ZookeeperUtil.serverRegister(port);
                ZookeeperUtil.watch();
            }else{
                bind(serverBootstrap, port + 1);
            }
        });
        return future;
    }
}
