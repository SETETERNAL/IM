package im.server.handler;

import im.model.JsonPacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;

/**
 * 异常处理
 */
@Slf4j
@ChannelHandler.Sharable
public class ExceptionCaughtHandler extends ChannelDuplexHandler {

    public final static ExceptionCaughtHandler INSTANCE = new ExceptionCaughtHandler();
    private ExceptionCaughtHandler(){}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务器异常", cause);
        ctx.channel().writeAndFlush(new JsonPacket(500, "服务器异常"));
    }
}
