package im.server.handler;

import com.google.gson.Gson;
import im.model.JsonPacket;
import im.model.LoginPacket;
import im.model.Member;
import im.server.ServerMain;
import im.server.commpent.redis.RedisKey;
import im.server.commpent.redis.RedisUtil;
import im.server.commpent.zookeeper.model.ServerRegister;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static im.server.commpent.ThreadPoolConfig.*;

/**
 * @author cch
 * @date 2021/6/17 18:06
 * 登录
 */
@ChannelHandler.Sharable
public class LoginServerHandler extends SimpleChannelInboundHandler<LoginPacket> {

    public final static LoginServerHandler INSTANCE = new LoginServerHandler();

    /**
     * 保存当前服务器上面的用户连接信息
     */
    public final static Map<String, ChannelHandlerContext> USER_MAP = new LinkedHashMap<>();
    /**
     * 服务器集群列表缓存
     */
    public final static Set<String> SERVER_ONLINE_SET = new CopyOnWriteArraySet<>();
    /**
     * 当前服务器的连接数
     */
    public final static AtomicInteger CONN_COUNT = new AtomicInteger(0);
    private LoginServerHandler(){}

    /**
     * @param userName 用户名称
     * @return
     * 根据用户名称获取用户信息
     */
    public static Member getMember(String userName){
        if(StringUtils.isEmpty(userName)){
            return null;
        }
        String memberStr = RedisUtil.getStr(RedisKey.USER_INFO_KEY + userName);
        return new Gson().fromJson(memberStr, Member.class);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LoginPacket loginPacket){
        if(loginPacket.getLoginName() != null && loginPacket.getPassword() != null){
            USER_LOGIN_THREAD_POOL.execute(() -> {
                Member member = getMember(loginPacket.getLoginName());
                if(member == null || StringUtils.isBlank(member.getPassword())){
                    ctx.channel().writeAndFlush(new JsonPacket(401, "用户不存在"));
                    return;
                }
                if(Objects.equals(member.getPassword(), loginPacket.getPassword())){
                    String token = UUID.randomUUID().toString().replace("-", "");
                    AuthHandler.putLoginStatus(ctx, new Member(loginPacket.getLoginName()));
                    ctx.channel().writeAndFlush(new JsonPacket(100,"登录成功", (Object) token));
                    // 设置服务器标识
                    RedisUtil.setStr(RedisKey.USER_SERVER_KEY + loginPacket.getLoginName(), new ServerRegister("127.0.0.1", ServerMain.port).toString());
                    // 记录分布式登录状态
                    RedisUtil.setStr(RedisKey.USER_LOGIN_TOKEN_KEY + token, new Gson().toJson(member));
                    // 发送离线消息
                    MESSAGE_CACHE_THREAD_POOL.execute(() -> {
                        sendOfflineMessage(ctx, loginPacket.getLoginName());
                    });
                }else{
                    // ctx.channel().writeAndFlush(msg)改为ctx.writeAndFlush(msg)，则会跳过所有的OutBoundHandler而直接写入到底层
                    ctx.writeAndFlush(new JsonPacket(401, "密码错误"));
                    return;
                }
            });
        }else{
            ctx.writeAndFlush(new JsonPacket(500, "用户名或密码为空"));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 在这里批量刷新可提升性能
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int count = CONN_COUNT.incrementAndGet();
        System.out.println("当前总连接数为:" + count);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CONN_COUNT.decrementAndGet();
        super.channelInactive(ctx);
    }

    /**
     * @param ctx
     * @param receiverId 收信人
     * 发送离线消息
     */
    public static void sendOfflineMessage(ChannelHandlerContext ctx, String receiverId){
        String redisKey = RedisKey.MESSAGE_KEY + receiverId;
        List<String> messageList;
        while((messageList = RedisUtil.batchLpop(redisKey, 10)).size() > 0){
            for (int i = 0; i < messageList.size(); i++) {
                String message = messageList.get(i);
                if (ctx.channel().isOpen()) {
                    // 这里如果使用write，将会影响消息的发送效率，影响用户的体验
                    // userCtx.writeAndFlush比 userCtx.channel().writeAndFlush能够缩短out路径
                    ctx.write(new JsonPacket(message))
                            .addListener(future -> {
                                if(!future.isSuccess()){
                                    // 发送失败重新加回队列
                                    RedisUtil.rpush(redisKey, message);
                                }
                            });
                }else{
                    // 连接断开重新放回队列里面
                    List<String> tmpMessageList = new ArrayList<>();
                    for(int j = i; j < messageList.size(); j++) {
                        tmpMessageList.add(messageList.get(j));
                    }
                    if(tmpMessageList.size() > 0) {
                        RedisUtil.rpush(redisKey, messageList.toArray(new String[0]));
                    }
                    return;
                }
            }
        }
    }

}
