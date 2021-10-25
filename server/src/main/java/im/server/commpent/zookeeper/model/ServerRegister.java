package im.server.commpent.zookeeper.model;

import com.google.gson.Gson;

/**
 * @author cch
 */
public class ServerRegister {
    private String host;
    private int port;

    public ServerRegister(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
