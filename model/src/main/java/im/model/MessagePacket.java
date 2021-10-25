package im.model;

import im.model.enums.CommandEnum;
import lombok.Getter;

/**
 * @author cch
 * @date 2021/6/18 16:36
 */
@Getter
public class MessagePacket extends Packet{

    /**
     * 接收人id
     */
    private String receiverId;
    /**
     *接收人类型
     */
    private Integer receiverType;
    /**
     * 消息内容
     */
    private String message;

    private boolean hasRpc = false;

    public MessagePacket(String message) {
        this.message = message;
    }

    public MessagePacket(String token, String message) {
        super.token = token;
        this.message = message;
    }

    public MessagePacket(String token, String receiverId, Integer receiverType, String message) {
        super.token = token;
        this.receiverId = receiverId;
        this.receiverType = receiverType;
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return CommandEnum.MESSAGE.getCode();
    }

    public boolean getHasRpc() {
        return hasRpc;
    }

    public void setHasRpc(boolean hasRpc) {
        this.hasRpc = hasRpc;
    }
}
