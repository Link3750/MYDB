package top.guoziyang.mydb.backend.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description 从properties中获取到启动参数
 * @Author 短途游
 * @Date 2024/8/13 17:13
 **/
public class PropertiesEntity {

    public static final int DEFAULT_PORT = 9999;

    private String path;

    private Integer port;

    private String mem;

    private final Properties properties;

    public PropertiesEntity() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mydb.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        properties.list(System.out);
        // 初始化properties
        this.properties = properties;

        // 数据库路径
        this.path = properties.getProperty("path");

        // 端口号
        String port = properties.getProperty("port");
        this.port = port == null || port.isEmpty() ? DEFAULT_PORT : Integer.parseInt(port);

        // 内存
        this.mem = properties.getProperty("mem");
    }

    public int getPort() {
        return port;
    }

    public String getMem() {
        return mem;
    }

    public String getDBPath() {
        return path;
    }

    public Properties getProperties() {
        return properties;
    }
}
