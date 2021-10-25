package im.model;

/**
 * @author cch
 * @date 2021/6/17 18:16
 */
public class JsonPacket extends Packet{

    private Integer code = 200;
    private String msg;
    private Object data;

    private final static JsonPacket NOT_LOGIN = new JsonPacket(401, "登录失效");
    public final static JsonPacket HEART_BEAT = new JsonPacket(204, "收到心跳");

    @Override
    public byte getCommand() {
        return 0;
    }

    public JsonPacket(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonPacket(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonPacket(Object data) {
        this.msg = "操作成功";
        this.data = data;
    }

    public JsonPacket(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    public JsonPacket(String msg) {
        this.code = 200;
        this.msg = msg;
    }

    public static JsonPacket notLogin(){
        return NOT_LOGIN;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
