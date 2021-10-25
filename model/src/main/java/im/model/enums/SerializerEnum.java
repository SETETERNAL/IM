package im.model.enums;


import im.model.JsonSerializer;
import im.model.Serializer;

/**
 * @author cch
 * @date 2021/6/17 16:45
 */
public enum SerializerEnum {

    FASTJSON((byte)1, "fastjson", new JsonSerializer())
    ;

    private final Byte code;
    private final String text;
    private final Serializer serializer;

    SerializerEnum(Byte code, String text, Serializer serializer) {
        this.code = code;
        this.text = text;
        this.serializer = serializer;
    }

    public Byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public static SerializerEnum get(byte code){
        for (SerializerEnum value : SerializerEnum.values()) {
            if(value.getCode() == code){
                return value;
            }
        }
        return FASTJSON;
    }
}
