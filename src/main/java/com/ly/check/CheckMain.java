package com.ly.check;


import java.io.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CheckMain {
    private static int count = 1;
    private static final ThreadPoolExecutor checkService = new ThreadPoolExecutor(10, 20, 1L, TimeUnit.SECONDS,
        new SynchronousQueue());

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: please enter file absolute path, eg. /tmp/kylin.log");
            return;
        }

        String file = args[0];
        System.out.println("file: " + file + ", parsing log.....................");
        String sqlstr = readFile(file);

        if (sqlstr == null) {
            System.out.println("parse error. stop!");
            return;
        }

        String[] sqlArray = sqlstr.split("#####");
        for (String sqlStr : sqlArray) {
            String sql = sqlStr.split("%%%%%")[0];
            String project = sqlStr.split("%%%%%")[1];
            Thread.sleep(1000L);
            checkService.submit(new DataConsistencyAction(project, sql));
        }

    }


    public static String readFile(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder sql = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("SQL:")) {
                    line = line.trim();
                    int len = line.length();
                    line = line.substring(4, len);
                    sql.append(line).append("\r\n");
                    while ((line = br.readLine()) != null) {
                        if (!line.startsWith("User:")) {
                            sql.append(line).append("\r\n");
                        } else {
                            if (!sql.toString().endsWith(";")) {
                                sql.append(";");
                            }
                            count++;
                            break;
                        }
                    }
                }
                if (line.startsWith("Project:")) {
                    sql.append("%%%%%");
                    int len = line.length();
                    sql.append(line.substring(8, len).trim());
                    sql.append("#####");
                }
            }
            System.out.println("find <" + count + "> sqls");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
