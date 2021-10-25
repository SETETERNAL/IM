package im.model.enums;

import im.model.*;

/**
 * @author cch
 * @date 2021/6/17 16:42
 */
public enum CommandEnum {
    JSON_RESULT((byte)0, "操作结果", JsonPacket.class),
    LOGIN((byte)1, "登录指令", LoginPacket.class),
    MESSAGE((byte)2, "消息传递", MessagePacket.class),
    HEART_BEAT((byte)3, "心跳包", HeartBeatPacket.class)
    ;

    private final Byte code;
    private final String text;
    private final Class<? extends Packet> packet;

    CommandEnum(Byte code, String text, Class<? extends Packet> packet) {
        this.code = code;
        this.text = text;
        this.packet = packet;
    }

    public Byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public Class<? extends Packet> getPacket() {
        return packet;
    }

    public static CommandEnum get(byte code){
        for (CommandEnum value : CommandEnum.values()) {
            if(code == value.getCode()){
                return value;
            }
        }
        return LOGIN;
    }
}
