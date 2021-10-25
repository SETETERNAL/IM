package im.server.handler;

import com.google.gson.Gson;
import im.model.Attributes;
import im.model.JsonPacket;
import im.model.Member;
import im.model.MessagePacket;
import im.server.commpent.redis.RedisKey;
import im.server.commpent.redis.RedisSingleUtil;
import im.server.commpent.rpc.client.RpcClient;
import im.server.commpent.zookeeper.model.ServerRegister;
import im.util.PacketEncoderAndDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static im.server.commpent.ThreadPoolConfig.MESSAGE_CACHE_THREAD_POOL;
import static im.server.handler.LoginServerHandler.*;

/**
 * @author cch
 * @date 2021/6/17 10:56
 * 消息发送
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerReceivedMessageHandler extends SimpleChannelInboundHandler<MessagePacket> {

    public final static ServerReceivedMessageHandler INSTANCE = new ServerReceivedMessageHandler();

    private ServerReceivedMessageHandler(){}

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessagePacket messagePacket) {
        log.warn("收到发送给 {} 消息:{}", messagePacket.getReceiverId(), messagePacket.getMessage());
        if(StringUtils.isBlank(messagePacket.getReceiverId())){
            ctx.writeAndFlush(new JsonPacket(500, "请输入你要发送的姓名！"));
            return;
        }
        ChannelHandlerContext userCtx = USER_MAP.get(messagePacket.getReceiverId());
        final String key = RedisKey.MESSAGE_KEY +messagePacket.getReceiverId();
        if(userCtx == null){
            String message = messagePacket.getMessage();
            MESSAGE_CACHE_THREAD_POOL.execute(() ->{
                Member receiverMember = LoginServerHandler.getMember(messagePacket.getReceiverId());
                if(receiverMember == null){
                    ctx.writeAndFlush(new JsonPacket(500, "用户不存在"));
                    return;
                }
                // 判断用户是否在其他服务器上面已登录
                String server = RedisSingleUtil.getStr(RedisKey.USER_SERVER_KEY + messagePacket.getReceiverId());
                if(StringUtils.isNotBlank(server) && SERVER_ONLINE_SET.contains(server)){
                    ServerRegister serverRegister = new Gson().fromJson(server, ServerRegister.class);
                    if(serverRegister != null && StringUtils.isNotBlank(serverRegister.getHost())){
                        messagePacket.setHasRpc(true);
                        RpcClient.valueOf(serverRegister.getHost(), serverRegister.getPort()).send(messagePacket, () -> {
                            RedisSingleUtil.rpush(key, message);
                        });
                    }
                }else{
                    RedisSingleUtil.rpush(key, message);
                }
            });
        }else {
            userCtx.writeAndFlush(new JsonPacket(messagePacket.getMessage())).addListener(future -> {
                if(!future.isSuccess()){
                    RedisSingleUtil.rpush(key, messagePacket.getMessage());
                }
            });
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 在这里批量刷新可提升性能
        // ctx.flush();
        super.channelReadComplete(ctx);
    }

}
