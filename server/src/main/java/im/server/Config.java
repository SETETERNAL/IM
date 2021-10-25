package im.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author cch
 * 读取配置，对应resources/config.properties
 */
public class Config {

    public final static Properties CONFIG = new Properties();

    static {
        InputStream inputStream = null;
        try {
            File file = new File("/data/im/config.properties");
            if (file.exists() && file.isFile()) {
                inputStream = new FileInputStream(file);
            } else {
                inputStream = ServerMain.class.getClassLoader().getResourceAsStream("config.properties");
            }
            CONFIG.load(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        } finally{
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
