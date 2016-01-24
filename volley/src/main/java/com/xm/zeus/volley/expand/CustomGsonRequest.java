package com.xm.zeus.volley.expand;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xm.zeus.volley.source.AuthFailureError;
import com.xm.zeus.volley.source.DefaultRetryPolicy;
import com.xm.zeus.volley.source.NetworkResponse;
import com.xm.zeus.volley.source.ParseError;
import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.Response;
import com.xm.zeus.volley.source.Response.ErrorListener;
import com.xm.zeus.volley.source.Response.Listener;
import com.xm.zeus.volley.source.toolbox.HttpHeaderParser;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 自定义Request，添加Cookie管理。返回值类型为T
 */
public class CustomGsonRequest<T> extends Request<T> {

    /**
     * 需要转化成的Gson类型
     */
    private final java.lang.reflect.Type mType;

    /**
     * 请求回调接口
     */
    private final Listener<T> mListener;

    /**
     * Post参数，get请求时，该参数为null
     */
    private final java.util.Map<java.lang.String, java.lang.String> mParams;

    //XML相关

    /**
     * @param method        请求类型，如Get、Post，详情请查看： {@link Method}
     * @param url           请求地址
     * @param type          请求返回值转换成Gson的类型
     * @param params        Post参数
     * @param listener      请求成功回调接口
     * @param errorListener 请求失败回调接口
     */
    public CustomGsonRequest(int method, java.lang.String url,
                             java.lang.reflect.Type type, java.util.Map<String, String> params,
                             Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mParams = params;
        mType = type;
    }

    /**
     * @param timeoutMs     超时时间（毫秒）
     * @param maxNumRetries 重连次数
     */
    public void setRetryPolicy(int timeoutMs, int maxNumRetries) {
        setRetryPolicy(new DefaultRetryPolicy(timeoutMs, maxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public byte[] getBody() throws AuthFailureError {


        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            try {
                return new JSONObject(params).toString().getBytes(HTTP.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * 重写父类方法，添加Post参数。
     *
     * @return Map<String, String>形式存储的参数
     */
    @Override
    protected java.util.Map<java.lang.String, java.lang.String> getParams()
            throws AuthFailureError {

        java.util.Map<java.lang.String, java.lang.String> params = super
                .getParams();

        if (params == null || params.equals(java.util.Collections.emptyMap())) {
            params = new java.util.HashMap<java.lang.String, java.lang.String>();
        }

        if (getMethod() == Method.POST && mParams != null) {
            params.putAll(mParams);
        }

        return params;
    }

    /**
     * 重写父类方法，将Cookie信息添加到请求头中。
     *
     * @return Map<String, String>形式存储的头文件
     */
    @Override
    public java.util.Map<java.lang.String, java.lang.String> getHeaders()
            throws AuthFailureError {
        java.util.Map<java.lang.String, java.lang.String> headers = super
                .getHeaders();

        if (headers == null || headers.equals(java.util.Collections.emptyMap())) {
            headers = new java.util.HashMap<java.lang.String, java.lang.String>();
        }

        CustomVolley.getInstance().addSessionCookie(headers);

        return headers;
    }

    /**
     * 重写父类方法，手动处理返回值格式，保存cookie。
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        CustomVolley.getInstance().checkSessionCookie(response.headers);

        return formattingData(response);

    }

    private Response<T> formattingData(NetworkResponse response) {

        try {
            return (Response<T>) Response.success(JSON.parseObject(new String(response.data, "utf-8"), mType), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset="
                + getParamsEncoding();
    }

}
