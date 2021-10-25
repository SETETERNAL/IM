package im.model;

public class ErrorMessagePacket extends Packet{

    @Override
    public byte getCommand() {
        return -1;
    }
}
