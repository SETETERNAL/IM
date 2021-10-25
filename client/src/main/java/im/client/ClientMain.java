package im.client;

import im.util.PacketDecoder;
import im.util.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author cch
 * @date 2021/6/17 10:43
 */
public class ClientMain {

    public final static Bootstrap bootstrap = new Bootstrap();
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 6665;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new PacketEncoder());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                        ch.pipeline().addLast(new ClientSendMessageHandler());
                    }
                });
        ChannelFuture future = connect().sync();
        future.channel().closeFuture();
    }

    public final static int MAX_RETRY = 3;

    public static ChannelFuture connect() throws ExecutionException, InterruptedException {
        return connect(bootstrap, HOST, PORT, MAX_RETRY);
    }


    public static ChannelFuture connect(final Bootstrap bootstrap, String host, int port, final int retryCount) throws InterruptedException, ExecutionException {
        ChannelFuture future = bootstrap.connect(host, port).await();
        if (future.isSuccess()) {
            System.out.println("连接成功");
            return future;
        } else {
            if (retryCount <= 0) {
                System.out.println("连接超时了，请检查网络是否连通");
                return future;
            } else {
                int order = (MAX_RETRY - retryCount) + 1;
                int delay = 1 << order;
                System.out.println("第" + order + "次连接失败了");
                ScheduledFuture<ChannelFuture> scheduledFuture = bootstrap.config().group().schedule(() ->
                        connect(bootstrap, host, port, retryCount - 1), delay, TimeUnit.SECONDS);
                return scheduledFuture.get();
            }
        }
    }
}
