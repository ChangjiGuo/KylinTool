package com.ly.common;

import java.io.IOException;
import java.util.Properties;

/**
 * description:
 *
 * @author changji.guo
 * @date 2020/3/20 10:53
 */
public class ConfigLoader {

    private ConfigLoader() {

    }

    public static final String CLUSTER1_JDBC_URL;
    public static final String CLUSTER1_USER;
    public static final String CLUSTER1_PWD;
    public static final String CLUSTER2_JDBC_URL;
    public static final String CLUSTER2_USER;
    public static final String CLUSTER2_PWD;
    public static final String CLUSTER1_HTTP_URL;
    public static final String CLUSTER2_HTTP_URL;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();
        try {
            prop.load(loader.getResourceAsStream("application.properties"));
        } catch (IOException e) {
            System.out.println("初始化配置文件失败! " + e);
        }
        CLUSTER1_JDBC_URL = prop.getProperty("KYLIN.CLUSTER1.JDBC.URL");
        CLUSTER1_USER = prop.getProperty("KYLIN.CLUSTER1.USERNAME");
        CLUSTER1_PWD = prop.getProperty("KYLIN.CLUSTER1.PASSWORD");
        CLUSTER2_JDBC_URL = prop.getProperty("KYLIN.CLUSTER2.JDBC.URL");
        CLUSTER2_USER = prop.getProperty("KYLIN.CLUSTER2.USERNAME");
        CLUSTER2_PWD = prop.getProperty("KYLIN.CLUSTER2.PASSWORD");
        CLUSTER1_HTTP_URL = prop.getProperty("KYLIN.CLUSTER1.HTTP.URL");
        CLUSTER2_HTTP_URL = prop.getProperty("KYLIN.CLUSTER2.HTTP.URL");
    }
}
