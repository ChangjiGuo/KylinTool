package com.ly.common;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * ClassName: Cube
 * Description:
 * Date: 2018/5/10 17:53
 *
 * @author jj48900
 * @version V1.0
 */
public class Cube {
    @JSONField(name = "name")
    private String cubeName;

    @JSONField(name = "owner")
    private String owner;

    public String getCubeName() {
        return cubeName;
    }

    public void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
