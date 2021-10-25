package im.server.handler;

import im.model.HeartBeatPacket;
import im.model.JsonPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端心跳包处理
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatPacket> {

    public final static HeartBeatHandler INSTANCE = new HeartBeatHandler();
    private HeartBeatHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatPacket msg) throws Exception {
        ctx.writeAndFlush(JsonPacket.HEART_BEAT);
//        ctx.write(JsonPacket.HEART_BEAT);
//        ctx.channel().writeAndFlush(JsonPacket.HEART_BEAT);
//        ctx.channel().write(JsonPacket.HEART_BEAT);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
