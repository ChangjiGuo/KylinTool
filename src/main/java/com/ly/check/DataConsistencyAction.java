package com.ly.check;

import com.ly.common.ConfigLoader;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ClassName: DataConsistency
 * Description:
 * Date: 2018/11/21 16:22
 *
 * @author jj48900
 * @version V1.0
 */
public class DataConsistencyAction implements Runnable {

    //老集群配置
    private final String OLD_CLUSTER = ConfigLoader.CLUSTER1_JDBC_URL;
    private final String OLD_USER_NAME = ConfigLoader.CLUSTER1_USER;
    private final String OLD_PASSWD = ConfigLoader.CLUSTER1_PWD;

    //新集群配置
    private final String NEW_CLUSTER = ConfigLoader.CLUSTER2_JDBC_URL;
    private final String NEW_USER_NAME = ConfigLoader.CLUSTER2_USER;
    private final String NEW_PASSWD = ConfigLoader.CLUSTER2_PWD;

    private String projectName;
    private String sql;

    public DataConsistencyAction(String projectName, String sql) {
        this.projectName = projectName;
        this.sql = sql;
    }

    private void docheck() {
        List<List<String>> oldData = null;
        try {
            oldData = getData(OLD_USER_NAME, OLD_PASSWD, OLD_CLUSTER);
        } catch (Exception e) {
            System.out.println("error :" + e);
        }
        List<List<String>> newData = null;
        try {
            newData = getData(NEW_USER_NAME, NEW_PASSWD, NEW_CLUSTER);
        } catch (Exception e) {
            System.out.println("error :" + e);
        }
        compare(oldData, newData);


    }


    public void compare(List<List<String>> oldData, List<List<String>> newData) {
        if (oldData.size() != newData.size()) {
            System.out.println("数据总量不一致:" + projectName + "\t" + sql);
            return;
        }
        for (int i = 0; i < oldData.size(); i++) {
            List<String> oldRow = oldData.get(i);
            List<String> newRow = newData.get(i);
            for (int j = 0; j < oldRow.size(); j++) {
                String oldCell = oldRow.get(j);
                String newCell = newRow.get(j);
                if (!oldCell.equals(newCell)) {
                    System.out.println("数据不一致:" + projectName + "\t" + sql);
                    System.out.println("oldData:" + formatData(oldRow));
                    System.out.println("newData:" + formatData(newRow));
                }
            }
        }
        System.out.println("check successful..");
    }

    private String formatData(List<String> row) {
        StringBuilder sb = new StringBuilder();
        for (String cell : row) {
            sb.append(cell).append("\t");
        }
        return sb.toString();
    }

    public List<List<String>> getData(String userName, String passwd, String url) throws Exception {
        Connection conn = getConn(userName, passwd, url + "/" + projectName);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery(sql);
        conn.close();
        List<List<String>> results = new ArrayList<>();
        while (resultSet.next()) {
            List<String> result = new ArrayList<>();
            int col = 1;
            while (true) {
                try {
                    result.add(resultSet.getString(col));
                } catch (SQLException e) {
                    break;
                }
                col++;
            }
            results.add(result);
        }
        return results;
    }

    public Connection getConn(String userName, String passwd, String url) throws Exception {
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();
        Properties info = new Properties();
        info.put("user", userName);
        info.put("password", passwd);
        return driver.connect(url, info);
    }


    @Override
    public void run() {
        docheck();
    }
}
