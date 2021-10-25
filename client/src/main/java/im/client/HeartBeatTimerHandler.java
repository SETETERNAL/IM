package im.client;

import im.model.HeartBeatPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳包
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
         sendHeartBeatTimer(ctx);
        super.channelActive(ctx);
    }

    private void sendHeartBeatTimer(ChannelHandlerContext ctx){
        ctx.executor().schedule(() -> {
            if(ctx.channel().isActive()){
                ctx.channel().writeAndFlush(new HeartBeatPacket()).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if(future.isSuccess()){
                            sendHeartBeatTimer(ctx);
                        }else{
                            // 失败重连
                            ConnectionUtil.connect(ctx.channel());
                        }
                    }
                });
            }
        }, 5, TimeUnit.SECONDS);
    }
}
