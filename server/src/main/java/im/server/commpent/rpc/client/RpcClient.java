package im.server.commpent.rpc.client;

import im.model.MessagePacket;
import im.util.PacketEncoderAndDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cch
 * rpc客户端
 */
@Slf4j
public class RpcClient {

    private String host;
    private Integer port;
    private EventLoopGroup eventExecutors;
    private Channel channel;
    private long activeTime = new Date().getTime();

    protected RpcClient(){}

    /**
     * @param host
     * @param port
     * @return
     * 获取一个RpcClient，运用池化技术节省资源
     */
    public static RpcClient valueOf(String host, Integer port){
        return RpcClientPool.get(host, port);
    }

    /**
     * 初始化客户端，这里将启动一个nio线程发送消息
     */
    protected void initBootstrap(){
        activeTime = new Date().getTime();
        // TODO n台服务器就要维护n个线程，不得不说开销不小
        eventExecutors = new NioEventLoopGroup(1, new DefaultThreadFactory(String.format("rpc-nio-client-%s-%s", host, port)));
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .remoteAddress(host, port)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(PacketEncoderAndDecoder.INSTANCE);
                        ch.pipeline().addLast(HeartBeatTimerHandler.INSTANCE);
                    }
                });
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.connect().sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message 消息主体
     * @param errorTask 错误机制，如果消息发送失败将运行这个事件
     * 发送消息
     */
    public void send(Object message, Runnable errorTask){
        activeTime = new Date().getTime();
        // TODO 服务器网络波动、宕机等情况还要再考虑一下
        channel.writeAndFlush(message).addListener(future -> {
            if(!future.isSuccess()){
                if(errorTask != null) {
                    channel.eventLoop().execute(errorTask);
                }
            }
        });
    }

    /**
     * 停止线程，现阶段只能管理池能调用这个方法
     */
    protected void shutdown(){
        if(eventExecutors != null){
            eventExecutors.shutdownGracefully();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(50);
        int i = 100;
        CountDownLatch downLatch = new CountDownLatch(i-1);
        for (int i1 = 0; i1 < i; i1++) {
            service.execute(() -> {
                downLatch.countDown();
                RpcClient rpcClient = RpcClient.valueOf("127.0.0.1", 6665);
                System.out.println(rpcClient);
                try {
                    downLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rpcClient.send(new MessagePacket("hahah"), new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("error");
                    }
                });
            });
        }
        service.shutdown();
    }

}
