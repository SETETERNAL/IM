package im.server.commpent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池配置
 */
public class ThreadPoolConfig {

    /**
     * 消息发送线程池
     */
    public final static ExecutorService MESSAGE_CACHE_THREAD_POOL = Executors.newWorkStealingPool();
    /**
     * 用户登录线程池
     */
     public final static ExecutorService USER_LOGIN_THREAD_POOL = Executors.newWorkStealingPool();
}
