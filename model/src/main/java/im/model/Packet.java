package im.model;

/**
 * @author cch
 * @date 2021/6/17 16:40
 */
public abstract class Packet {

    public String token;
    public byte version;
    public abstract byte getCommand();
}