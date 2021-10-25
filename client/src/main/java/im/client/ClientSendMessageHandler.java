package im.client;

import im.model.JsonPacket;
import im.model.MessagePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;
import java.util.Scanner;


/**
 * @author cch
 * @date 2021/6/17 10:50
 */
@Slf4j
public class ClientSendMessageHandler extends SimpleChannelInboundHandler<JsonPacket> {

    public static String token = null;
    private static String receiverId = "";
    private static boolean consoleIsStartUp = false;
    private static Channel CHANNEL;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        startConsoleThread(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, JsonPacket packet){
        if(packet.getCode() == 100){
            token = (String)packet.getData();
            System.out.println(new Date() + "登录成功");
        }else if(packet.getCode() == 200){
            System.out.println(new Date() + "客户端收到信息:" + packet.getMsg());
        }else if(packet.getCode() == 401){
            System.out.println(new Date() + packet.getMsg());
            token = null;
            LoginInfoDTO.username = "";
            LoginInfoDTO.password = "";
        }else if(packet.getCode() == 204){
        }else{
            System.out.println(new Date() + packet.getMsg());
        }
    }


    private static void startConsoleThread(Channel channel) {
        log.warn("channel:{}", channel);
        CHANNEL = channel;
        if(!consoleIsStartUp) {
            new Thread(() -> {
                consoleIsStartUp = true;
                while (!Thread.interrupted()) {
                    if (token != null) {
                        if (!CHANNEL.isOpen()) {
                            // 重新连接
                            ConnectionUtil.restConnect(CHANNEL);
                        }
                        while (receiverId == "" || receiverId == null) {
                            System.out.println("请输入你要发送的姓名: ");
                            Scanner nameSc = new Scanner(System.in);
                            receiverId = nameSc.nextLine();
                        }
                        System.out.println("输入消息（可输入&1切换用户）: ");
                        Scanner sc = new Scanner(System.in);
                        String line = sc.nextLine();
                        if (Objects.equals(line, "&1")) {
                            receiverId = "";
                            while (receiverId == "" || receiverId == null) {
                                System.out.println("请输入你要发送的姓名: ");
                                Scanner nameSc = new Scanner(System.in);
                                receiverId = nameSc.nextLine();
                            }
                            System.out.println("输入消息（可输入&1切换用户）: ");
                            sc = new Scanner(System.in);
                            line = sc.nextLine();
                        }
                        if(Objects.equals(LoginInfoDTO.username, receiverId)){
                            System.out.println("你不能给自己发送消息！");
                        }else {
                            for (int i = 0; i < 10000; i++) {
                                MessagePacket packet = new MessagePacket(token, receiverId, 1, line + i);
                                ConnectionUtil.sendMessage(CHANNEL, packet);
                            }
                        }
                    } else {
                        ConnectionUtil.connect(CHANNEL);
                    }
                }
            }).start();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
        super.exceptionCaught(ctx, cause);
    }
}
