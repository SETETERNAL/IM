package im.server.handler;

import im.model.ErrorMessagePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ErrorMessageHandler extends SimpleChannelInboundHandler<ErrorMessagePacket> {

    public final static ErrorMessageHandler INSTANCE = new ErrorMessageHandler();
    private ErrorMessageHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ErrorMessagePacket errorMessagePacket) throws Exception {
        log.warn("数据错误");
    }
}
