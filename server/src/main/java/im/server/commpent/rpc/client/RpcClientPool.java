package im.server.commpent.rpc.client;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author cch
 * rpc client池化
 */
public class RpcClientPool {

    protected final static Map<String, RpcClient> POOL = new ConcurrentHashMap<>();
    private final static ScheduledExecutorService POOL_MANAGER = Executors.newScheduledThreadPool(2, new DefaultThreadFactory("RpcClientPool-manager"));
    private static long keepAliveTime = 60000;

    static {
        timerClear();
    }

    /**
     * @param host
     * @param port
     * @return
     * 从管理池中获取一个rpc client，如果没有就新增
     */
    public static RpcClient get(String host, Integer port){
        String key = String.format("%s:%s", host, port);
        RpcClient empty = new RpcClient();
        RpcClient client = RpcClientPool.POOL.putIfAbsent(key, empty);
        if(client == null){
            empty.setHost(host);
            empty.setPort(port);
            empty.initBootstrap();
            return empty;
        }
        return client;
    }

    /**
     * 定时清理无用的rpc client
     */
    public static void timerClear(){
        // 每60s将清理无用的线程
        POOL_MANAGER.scheduleAtFixedRate(() -> {
            remove();
        }, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 删除无用的rpc client
     */
    public static void remove(){
        long nowTime = new Date().getTime();
        for (String key : POOL.keySet()) {
            final RpcClient value = POOL.get(key);
            if (value != null) {
                if ((value.getActiveTime() + keepAliveTime) < nowTime) {
                    POOL.remove(key);
                    // 60s后将停止线程
                    POOL_MANAGER.schedule(() -> {
                        value.shutdown();
                    }, 60, TimeUnit.SECONDS);
                }
            }
        }
    }
}
