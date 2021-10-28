package im.server.handler;

import com.google.gson.Gson;
import im.model.*;
import im.server.ServerMain;
import im.server.commpent.redis.RedisKey;
import im.server.commpent.redis.RedisSingleUtil;
import im.server.commpent.zookeeper.model.ServerRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static im.server.commpent.ThreadPoolConfig.MESSAGE_CACHE_THREAD_POOL;
import static im.server.handler.LoginServerHandler.USER_MAP;

/**
 * 登录校验
 */
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<Packet> {

    public final static AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
        if(msg instanceof MessagePacket) {
            // TODO rpc的判断先简单实现,这种方式是设计的缺陷，后面优化
            boolean hasRpc = (msg instanceof MessagePacket && ((MessagePacket)msg).getHasRpc());
            if (hasRpc || hasLogin(ctx.channel())) {
                ctx.fireChannelRead(msg);
            } else {
                Member member = getMember(msg.token);
                if(getMember(msg.token) != null){
                    putLoginStatus(ctx, member);
                    MESSAGE_CACHE_THREAD_POOL.execute(() -> {
                        // 服务器宕机后重新给用户分配服务器
                        RedisSingleUtil.setStr(RedisKey.USER_SERVER_KEY + member.getUsername(), new ServerRegister("127.0.0.1", ServerMain.port).toString());
                        // 发送离线消息
                        LoginServerHandler.sendOfflineMessage(ctx, member.getUsername());
                    });
                    ctx.fireChannelRead(msg);
                }else {
                    ctx.channel().writeAndFlush(JsonPacket.notLogin());
                }
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    public static boolean hasLogin(Channel channel){
        Attribute<Boolean> attribute = channel.attr(Attributes.HAS_LOGIN);
        Member member = getMember(channel);
        boolean flag = (attribute != null && attribute.get() != null && attribute.get())
                && (member != null && USER_MAP.containsKey(member.getUsername()));
        if(flag){
            ChannelHandlerContext ctx = USER_MAP.get(member.getUsername());
            flag = Objects.equals(ctx.channel().id(), channel.id());
        }
        return  flag;
    }

    /**
     * @param token
     * @return
     * TODO 这里存在IO操作，可能阻塞线程
     */
    public static Member getMember(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String member = RedisSingleUtil.getStr(RedisKey.USER_LOGIN_TOKEN_KEY + token);
        if(StringUtils.isBlank(member)){
            return null;
        }
        Member model = new Gson().fromJson(member, Member.class);
        return model;
    }

    public static void putLoginStatus(ChannelHandlerContext ctx, Member member){
        USER_MAP.put(member.getUsername(), ctx);
        ctx.channel().attr(Attributes.HAS_LOGIN).set(true);
        ctx.channel().attr(Attributes.MEMBER_INFO).set(member);
    }

    public static Member getMember(Channel channel){
        return channel.attr(Attributes.MEMBER_INFO).get();
    }

}
