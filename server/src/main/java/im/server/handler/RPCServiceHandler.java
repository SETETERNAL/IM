package im.server.handler;

import im.model.JsonPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author cch
 */
@ChannelHandler.Sharable
public class RPCServiceHandler extends SimpleChannelInboundHandler<JsonPacket> {

    public final static RPCServiceHandler INSTANCE = new RPCServiceHandler();
    private RPCServiceHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JsonPacket msg) throws Exception {
        if(msg.getCode() == 200){
            System.out.println("处理成功");
        }else{
            System.out.println("处理失败,失败原因:" + msg.getMsg());
        }
    }
}
