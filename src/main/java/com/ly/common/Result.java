package com.ly.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Result {
    private int status;
    private String data;

    public Result() {
        status = 200;
        JSONObject json = new JSONObject();
        json.put("status", 200);
        this.data = json.toJSONString();
    }


    public String toString() {
        return data;
    }

    public JSONObject formatToJson() {
        JSONObject json = JSONObject.parseObject(data);
        return json;
    }

    public JSONArray formatToArray() {
        JSONArray array = JSONArray.parseArray(data);
        return array;
    }

    public <T> T formatToBean(Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    public <T> List<T> formatToList(Class<T> clazz) {
        return JSON.parseArray(data, clazz);
    }

    public boolean isSuccess() {
        return status == 200;
    }
    //+++++++++++++++++setter、getter++++++++++++++++++++++++

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
