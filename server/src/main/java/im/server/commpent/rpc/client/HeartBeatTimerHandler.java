package im.server.commpent.rpc.client;

import im.model.HeartBeatPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


/**
 * @author cch
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    public final static HeartBeatTimerHandler INSTANCE = new HeartBeatTimerHandler();

    private HeartBeatTimerHandler(){}

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
            ctx.channel().writeAndFlush(new HeartBeatPacket()).addListener(future -> {
               if(!future.isSuccess()){
                   log.info("客户端心跳发送失败");
               }
            });
        }, 5,5, TimeUnit.SECONDS);
    }
}
