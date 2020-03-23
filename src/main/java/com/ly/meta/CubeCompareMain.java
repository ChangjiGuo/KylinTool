package com.ly.meta;

import com.ly.common.ApiEnum;
import com.ly.common.ConfigLoader;
import com.ly.common.Cube;
import com.ly.common.HttpTool;
import com.ly.common.Project;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * description:
 *
 * @author changji.guo
 * @date 2020/3/20 10:28
 */
public class CubeCompareMain {

    private static final Logger logger = LoggerFactory.getLogger(CubeCompareMain.class);

    public static void main(String[] args) {
        doCompare();
    }

    private static void doCompare() {
        //获取老集群所有项目
        List<Project> projects = getProjectList();
        List<Cube> oldCubes = new ArrayList<>();
        List<Cube> newCubes = new ArrayList<>();
        List<String> oldCubeNames = new ArrayList<>();
        List<String> newCubeNames = new ArrayList<>();
        for (Project project : projects) {
            oldCubes.addAll(getCubes(ConfigLoader.CLUSTER1_HTTP_URL, ConfigLoader.CLUSTER1_USER, ConfigLoader.CLUSTER1_PWD,
                project.getProjectName()));
            newCubes.addAll(getCubes(ConfigLoader.CLUSTER2_HTTP_URL, ConfigLoader.CLUSTER2_USER, ConfigLoader.CLUSTER2_PWD,
                project.getProjectName()));
        }
        logger.info("old cube count ---> " + oldCubes.size());
        logger.info("new cube count ---> " + newCubes.size());
        logger.info("start to compare............");

        oldCubes.forEach(cube -> oldCubeNames.add(cube.getCubeName()));
        newCubes.forEach(cube -> newCubeNames.add(cube.getCubeName()));

        for (String cubeName : oldCubeNames) {
            if (newCubeNames.contains(cubeName)) {
                logger.info(cubeName + " alredy exist!");
            } else {
                logger.warn(cubeName + " not in new cluster, please check......");
            }
        }
    }

    private static List<Project> getProjectList() {
        HttpTool httpTool = new HttpTool(ConfigLoader.CLUSTER1_HTTP_URL, ConfigLoader.CLUSTER1_USER,
            ConfigLoader.CLUSTER1_PWD);
        httpTool.setApiEnum(ApiEnum.KYLIN_PROJECTS);
        logger.info("status: {}",httpTool.doGet().getStatus());
        return httpTool.doGet().formatToList(Project.class);
    }

    private static List<Cube> getCubes(String clusterUrl, String userName, String userPassword, String projectName) {
        HttpTool httpTool = new HttpTool(clusterUrl, userName, userPassword);
        Map<String, String> params = Maps.newHashMap();
        params.put("projectName", projectName);
        params.put("offset", String.valueOf(0));
        params.put("limit", String.valueOf(Integer.MAX_VALUE));
        httpTool.setParams(params);
        httpTool.setApiEnum(ApiEnum.KYLIN_CUBES);
        return httpTool.doGet().formatToList(Cube.class);
    }
}
