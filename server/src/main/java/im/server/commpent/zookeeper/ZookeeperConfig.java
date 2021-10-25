package im.server.commpent.zookeeper;

import im.server.Config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author cch
 */
public class ZookeeperConfig {

    private static volatile CuratorFramework curatorFramework = null;

    public static CuratorFramework getClient(){
        if(curatorFramework == null) {
            synchronized (ZookeeperUtil.class) {
                if(curatorFramework == null) {
                    curatorFramework = CuratorFrameworkFactory.
                            builder().connectString(Config.CONFIG.getProperty("zookeeper.address"))
                            .sessionTimeoutMs(Integer.valueOf(Config.CONFIG.getProperty("zookeeper.sessionTimeout")))
                            .retryPolicy(new ExponentialBackoffRetry(3000, 300))
                            .namespace("")
                            .build();
                    curatorFramework.start();
                }
            }
        }
        return curatorFramework;
    }
}
