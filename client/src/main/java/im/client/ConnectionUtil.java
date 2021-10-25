package im.client;

import im.model.LoginPacket;
import im.model.Packet;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ConnectionUtil {

    public static void restConnect(Channel channel){
        // 重新连接
        try {
            ClientMain.connect();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connect(channel);
    }

    public static void connect(Channel channel){
        while (StringUtils.isEmpty(LoginInfoDTO.username)) {
            System.out.println("请输入用户名: ");
            Scanner userNameSc = new Scanner(System.in);
            LoginInfoDTO.username = userNameSc.nextLine();
        }
         while (StringUtils.isEmpty(LoginInfoDTO.password)) {
            System.out.println("请输入密码: ");
            Scanner passwordSc = new Scanner(System.in);
            LoginInfoDTO.password = passwordSc.nextLine();
        }
        sendMessage(channel, new LoginPacket(LoginInfoDTO.username, LoginInfoDTO.password));
        try {
            // TODO 需要等待response处理完成，后面优化
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Channel channel, Packet packet){
        if(channel.isOpen()){
            try {
                channel.writeAndFlush(packet).await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                ClientMain.connect().addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if(future.isSuccess()){
                            channel.writeAndFlush(packet);
                        }else{
                            log.error("重新链接失败");
                        }
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
