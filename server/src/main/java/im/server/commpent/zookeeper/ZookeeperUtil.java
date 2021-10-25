package im.server.commpent.zookeeper;

import com.google.gson.Gson;
import im.server.commpent.rpc.client.RpcClient;
import im.server.commpent.zookeeper.model.ServerRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static im.server.handler.LoginServerHandler.SERVER_ONLINE_SET;

/**
 * @author cch
 */
@Slf4j
public class ZookeeperUtil {

    private final static CuratorFramework curatorFramework = ZookeeperConfig.getClient();
    private final static String PATH = "/im";
    private static String LOCAL_HOST = "";
    private static ServerRegister service = null;

    private final static void init(){
        try {
            ArrayList list = (ArrayList) curatorFramework.getChildren().forPath(PATH);
            for (Object o : list) {
                String childPath = (String)o;
                String childData = new String(curatorFramework.getData().forPath(String.format("%s/%s", PATH, childPath)));
                if(!Objects.equals(childData, new Gson().toJson(service))) {
                    addPoint(childData);
                }
            }
            log.info("zookeeper初始化完成，结果：{}", SERVER_ONLINE_SET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param service
     * @return
     * 命名规则
     */
    public static String name(ServerRegister service){
//        String forPath = String.format("%s/%s:%s", PATH, service.getHost(), service.getPort());
//        return forPath;
        return String.format("%s/%s", PATH, UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public static String name(String host, int port){
        return String.format("%s:%s", host, port);
    }

    public static void serverRegister(Integer port){
        serverRegister(port, 0);
        return;
    }

    public static void serverRegister(Integer port, int restCount) {
        try {
            ++restCount;
            service = new ServerRegister("127.0.0.1", port);
            LOCAL_HOST = name(service.getHost(), service.getPort());
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(name(service), new Gson().toJson(service).getBytes());
        }catch (Exception e){
            log.error("zookeeper第{}次注册错误", restCount, e);
            if(restCount < 3) {
                serverRegister(port, restCount);
            }
        }
        return;
    }

    public static void watch(){
        watch((client, event) -> {
            ChildData childData = event.getData();
            if(childData != null){
                log.info("watch前服务列表集合：{}", new Gson().toJson(SERVER_ONLINE_SET));
                switch (event.getType()){
                    case CHILD_ADDED:
                        log.info("正在新增子节点:{}", childData.getPath());
                        addPoint(childData.getData());
                        break;
                    case CHILD_UPDATED:
                        log.info("正在更新子节点:{}", childData.getPath());
                        addPoint(childData.getData());
                        break;
                    case CHILD_REMOVED:
                        log.info("子节点被删除:{}", childData.getPath());
                        removePoint(childData.getData());
                        break;
                    case CONNECTION_LOST:
                        log.info("连接丢失:{}", childData.getPath());
//                        removePoint(childData.getData());
                        break;
                    case CONNECTION_SUSPENDED:
                        log.info("连接被挂起:{}", childData.getPath());
//                        removePoint(childData.getData());
                        break;
                    case CONNECTION_RECONNECTED:
                        log.info("恢复连接:{}", childData.getPath());
//                        addPoint(childData.getData());
                        break;
                }
                init();
                log.info("watch后服务列表集合:{}", new Gson().toJson(SERVER_ONLINE_SET));
            }
        });
        init();
    }

    public static void addPoint(byte[] data){
        if(data != null) {
            addPoint(new String(data));
        }
    }

    public static void addPoint(String data){
        if(StringUtils.isNotBlank(data)) {
            SERVER_ONLINE_SET.add(data);
            ServerRegister serverRegister = new Gson().fromJson(data, ServerRegister.class);
            if(serverRegister != null){
                // RpcClient.register(serverRegister.getHost(), serverRegister.getPort());
            }
        }
    }

    public static void removePoint(byte[] data){
        if(data != null) {
            String str = new String(data);
            ServerRegister serverRegister = new Gson().fromJson(str, ServerRegister.class);
            if(serverRegister != null){
                SERVER_ONLINE_SET.remove(str);
                // RpcClient.cancel(serverRegister.getHost(), serverRegister.getPort());
            }
        }
    }

    private static PathChildrenCache watch(PathChildrenCacheListener listener){
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, PATH, true);
        pathChildrenCache.getListenable().addListener(listener);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathChildrenCache;
    }

}
