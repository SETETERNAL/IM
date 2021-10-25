package im.model;

import im.model.enums.CommandEnum;

/**
 * 心跳包
 */
public class HeartBeatPacket extends Packet{

    @Override
    public byte getCommand() {
        return CommandEnum.HEART_BEAT.getCode();
    }
}
