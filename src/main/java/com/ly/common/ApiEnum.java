package com.ly.common;


/**
 * description:
 *
 * @author changji.guo
 * @date 2020/3/20 11:34
 */
public enum ApiEnum {

    /**
     * 获取kylin项目信息
     */
    KYLIN_PROJECTS("/api/projects/readable", false),

    /**
     * 获取kylin的cube信息
     */
    KYLIN_CUBES("/api/cubes", false);

    private String apiPath;
    private boolean replaceParam;

    ApiEnum(String apiPath, boolean replaceParam) {
        this.apiPath = apiPath;
        this.replaceParam = replaceParam;
    }

    public String getApiPath() {
        return apiPath;
    }

    public boolean isReplaceParam() {
        return replaceParam;
    }
}
