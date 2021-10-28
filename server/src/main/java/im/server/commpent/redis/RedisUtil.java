package im.server.commpent.redis;

import com.google.gson.Gson;
import im.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author cch
 * redis组件，需要操作redis的公用方法统一写在这里
 */
@Slf4j
public class RedisUtil {

    private final static RedisClient redisClient = RedisClient.builder.newClient();


    public static String getStr(String key) {
        if(key == null || key.length() == 0){
            return null;
        }
        return new String(redisClient.get().get(key.getBytes()));
    }

    public static boolean setStr(String key, String value){
        return setStr(key, value, null);
    }

    public static boolean setStr(String key, String value, Long seconds){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
            return false;
        }
        if(seconds == null){
            redisClient.get().set(key.getBytes(), value.getBytes());
        }else{
            redisClient.get().set(key.getBytes(), value.getBytes(), SetParams.setParams().ex(seconds));
        }
        return true;
    }

    public static boolean rpush(String key, String... values){
        if (key == null || values == null || values.length == 0) {
            return false;
        }
        List<byte[]> valueList = new ArrayList<>();
        for (String value : values) {
            valueList.add(value.getBytes());
        }
        redisClient.get().rpush(key.getBytes(), valueList.toArray(new byte[0][0]));
        return true;
    }

    /**
     * @param key
     * @param count
     * @return
     * 批量左pop list里面的元素（该方法线程不安全）
     */
    public static List<String> batchLpop(String key, int count){
        if(key == null || key.length() == 0 || count < 0){
            return new ArrayList<>();
        }
        List<byte[]> resultList = redisClient.get().lrange(key.getBytes(), 0, count);
        return resultList.stream().map(obj -> new String(obj)).collect(Collectors.toList());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        setStr(RedisKey.USER_INFO_KEY+"cch", new Gson().toJson(new Member(1L, "cch", "123456")));
        setStr(RedisKey.USER_INFO_KEY+"libai", new Gson().toJson(new Member(2L, "libai", "123456")));
        setStr(RedisKey.USER_INFO_KEY+"dufu", new Gson().toJson(new Member(3L, "dufu", "123456")));
        System.out.println(getStr(RedisKey.USER_INFO_KEY+"cch"));
    }
}
