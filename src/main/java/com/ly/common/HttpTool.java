package com.ly.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpTool {
    private final Logger logger = LoggerFactory.getLogger(HttpTool.class);

    private static final Pattern URL_PATTERN = Pattern.compile("(\\{(\\w+)\\})");

    private final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    private String baseUrl;
    private String url;
    private ApiEnum apiEnum;
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private HttpEntity httpEntity;

    public HttpTool(String clusterUrl, String userName, String userPassword) {
        String auth = userName + ":" + userPassword;
        String basicAuth = "Basic " + new String(Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII)));
        this.headers.put("Authorization", basicAuth);
        this.headers.put("Content-Type", "application/json;charset=utf-8");
        this.baseUrl = clusterUrl;
    }

    public Result doPost(JSONObject requestBody) {
        httpEntity = getEntity(requestBody);
        return exec(RequestType.POST);
    }

    public Result doPut() {
        return exec(RequestType.PUT);
    }

    public Result doPut(JSONObject requestBody) {
        httpEntity = getEntity(requestBody);
        return exec(RequestType.PUT);
    }


    public Result doDel() {
        return exec(RequestType.DEL);
    }

    public Result doGet() {
        return exec(RequestType.GET);
    }

    private HttpEntity getEntity(JSONObject data) {
        if (data != null) {
            StringEntity requestEntity = new StringEntity(data.toJSONString(), "utf-8");
            requestEntity.setContentEncoding("utf-8");
            return requestEntity;
        }
        return null;
    }

    private Result exec(RequestType requestType) {
        try {
            initUrl();
            logger.info("start visit:{}", url);
            CloseableHttpResponse response = null;
            switch (requestType) {
                case DEL:
                    HttpDelete httpDelete = new HttpDelete(url);
                    response = closeableHttpClient.execute(httpDelete);
                    break;
                case GET:
                    HttpGet httpGet = new HttpGet(url);
                    addHeaders(httpGet);
                    response = closeableHttpClient.execute(httpGet);
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(httpEntity);
                    addHeaders(httpPut);
                    response = closeableHttpClient.execute(httpPut);
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(httpEntity);
                    addHeaders(httpPost);
                    response = closeableHttpClient.execute(httpPost);
                    break;
                default:
                    throw new IllegalArgumentException("暂不支持该类型的http请求,requestType:" + requestType);
            }
            return doResponse(response);
        } catch (Exception e) {
            logger.error(String.format("服务连接异常,url:%s,method:%s", url, requestType), e);
            return null;
        }
    }

    private void addHeaders(HttpRequestBase requestBase) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private void initUrl() {
        StringBuilder sb = new StringBuilder();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        sb.append(baseUrl);
        if (apiEnum == null) {
            this.url = baseUrl;
            return;
        }
        String resourcePath = apiEnum.getApiPath();
        if (params != null && params.size() > 0) {
            if (apiEnum.isReplaceParam() && StringUtils.isNotEmpty(resourcePath)) {
                Matcher matcher = URL_PATTERN.matcher(resourcePath);
                while (matcher.find()) {
                    resourcePath = resourcePath.replace(matcher.group(1), params.get(matcher.group(2)));
                }
                sb.append(resourcePath);
            } else {
                sb.append(resourcePath).append("?").append(Joiner.on("&").withKeyValueSeparator("=").join(params));
            }
        } else {
            sb.append(resourcePath);
        }
        this.url = sb.toString();
    }

    private Result doResponse(CloseableHttpResponse response) throws IOException {
        Result result = new Result();
        StatusLine statusLine = response.getStatusLine();
        int status = statusLine.getStatusCode();
        HttpEntity entity = response.getEntity();
        result.setData(EntityUtils.toString(entity));
        result.setStatus(status);
        return result;
    }

    private enum RequestType {
        POST, GET, DEL, PUT
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ApiEnum getApiEnum() {
        return apiEnum;
    }

    public void setApiEnum(ApiEnum apiEnum) {
        this.apiEnum = apiEnum;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }
}
