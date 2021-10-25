package im.model;

import com.google.gson.Gson;
import im.model.enums.SerializerEnum;

/**
 * @author cch
 * @date 2021/6/17 16:44
 */
public class JsonSerializer implements Serializer{

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerEnum.FASTJSON.getCode();
    }

    @Override
    public byte[] serialize(Object object) {
        return new Gson().toJson(object).getBytes();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {;
        return new Gson().fromJson(new String(bytes), clazz);
    }
}
