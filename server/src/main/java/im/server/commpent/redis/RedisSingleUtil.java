package im.server.commpent.redis;

import com.google.gson.Gson;
import im.model.Member;
import im.server.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author cch
 * 单机版redis组件（TODO 先实现一个单机版，后面再扩展集群）
 */
@Slf4j
public class RedisSingleUtil {

    private static JedisPool jedisPool;

    static {
        try {
            jedisPool = getPool();
        } catch (Exception e) {
            log.error("redis池异常：" + e.getMessage());
        }
    }

    private static JedisPool getPool() {
        if (jedisPool == null) {
            synchronized(RedisSingleUtil.class) {
                if(jedisPool == null) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(Integer.valueOf(Config.CONFIG.getProperty("redis.maxIdle")));
                    config.setMaxTotal(Integer.valueOf(Config.CONFIG.getProperty("redis.maxTotal")));
                    config.setMaxWaitMillis(Integer.valueOf(Config.CONFIG.getProperty("redis.maxWait")));
                    config.setTestOnBorrow(false);
                    config.setTestOnReturn(false);
                    jedisPool = new JedisPool(config, Config.CONFIG.getProperty("redis.host"), Integer.valueOf(Config.CONFIG.getProperty("redis.port")), 5000, Config.CONFIG.getProperty("redis.password"));
                }
            }
        }
        return jedisPool;
    }

    public static Jedis getJedis(){
        return getPool().getResource();
    }

    public static String getStr(String key) {
        if(key == null || key.length() == 0){
            return null;
        }
        try (Jedis jedis = getJedis()){
            String result = jedis.get(key);
            return result;
        }
    }

    public static boolean setStr(String key, String value){
        return setStr(key, value, null);
    }

    public static boolean setStr(String key, String value, Long seconds){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
            return false;
        }
        try(Jedis jedis = getJedis()) {
            if (seconds == null || seconds.compareTo(0L) <= 0) {
                jedis.set(key, value);
            } else {
                jedis.setex(key, seconds, value);
            }
        }
        return true;
    }

    public static boolean rpush(String key, String... values){
        if (key == null || values == null || values.length == 0) {
            return false;
        }
        try (Jedis jedis = getJedis()){
            if (key == null || values == null || values.length == 0) {
                return false;
            }
            return jedis.rpush(key, values).compareTo((long) values.length) == 0;
        }
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
        try(Jedis jedis = getJedis()){
            List<String> resultList = jedis.lrange(key, 0, count);
            jedis.ltrim(key, count, -1);
            return resultList;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        for(int i = 0; i < 2000;i++) {
//            getStr("user_server_cch");
//        }
        setStr(RedisKey.USER_INFO_KEY+"cch", new Gson().toJson(new Member(1L, "cch", "123456")));
        setStr(RedisKey.USER_INFO_KEY+"libai", new Gson().toJson(new Member(2L, "libai", "123456")));
        setStr(RedisKey.USER_INFO_KEY+"dufu", new Gson().toJson(new Member(3L, "dufu", "123456")));
    }
}
