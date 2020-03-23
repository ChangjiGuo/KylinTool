package com.ly.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * ClassName: Project
 * Description:
 * Date: 2018/5/11 15:10
 *
 * @author jj48900
 * @version V1.0
 */
public class Project {
    /**
     * 项目id
     */
    @JSONField(name = "uuid")
    private String projectId;

    /**
     * 项目名称
     */
    @JSONField(name = "name")
    private String projectName;

    /**
     * 项目所属者
     */
    @JSONField(name = "owner")
    private String owner;

    /**
     * 项目状态
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 项目下属models信息
     */
    @JSONField(name = "models")
    private List<String> models;

    /**
     * 项目所有的cube信息
     */
    @JSONField(name = "realizations", jsonDirect = true)
    private List<String> cubes;

    /**
     * 获取表名称
     */
    @JSONField(name = "tables")
    private List<String> tables;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public List<String> getCubes() {
        return cubes;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public void setCubes(String cubes) {
        JSONArray array = JSONObject.parseArray(cubes);
        if (array == null) {
            return;
        }
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < array.size(); i++) {
            JSONObject cube = array.getJSONObject(i);
            if ("CUBE".equals(cube.getString("type"))) {
                list.add(cube.getString("realization"));
            }
        }
        this.cubes = list;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Project{");
        sb.append("projectId='").append(projectId).append('\'');
        sb.append(", projectName='").append(projectName).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", models=").append(models);
        sb.append(", cubes=").append(cubes);
        sb.append(", tables=").append(tables);
        sb.append('}');
        return sb.toString();
    }
}
