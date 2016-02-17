package com.xm.zeus.volley.expand;

import com.alibaba.fastjson.JSON;
import com.xm.zeus.volley.entity.BaseEntity;
import com.xm.zeus.volley.exception.DataError;
import com.xm.zeus.volley.source.AuthFailureError;
import com.xm.zeus.volley.source.DefaultRetryPolicy;
import com.xm.zeus.volley.source.NetworkResponse;
import com.xm.zeus.volley.source.ParseError;
import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.Response;
import com.xm.zeus.volley.source.Response.ErrorListener;
import com.xm.zeus.volley.source.Response.Listener;
import com.xm.zeus.volley.source.VolleyError;
import com.xm.zeus.volley.source.toolbox.HttpHeaderParser;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 自定义Request，添加Cookie管理。返回值类型为T
 */
public class CustomStringRequest extends Request<String> {

    /**
     * 请求回调接口
     */
    private final Listener<String> mListener;

    /**
     * Post参数，get请求时，该参数为null
     */
    private final Map<String, String> mParams;

    //XML相关

    /**
     * @param method        请求类型，如Get、Post，详情请查看： {@link Method}
     * @param url           请求地址
     * @param params        Post参数
     * @param listener      请求成功回调接口
     * @param errorListener 请求失败回调接口
     */
    public CustomStringRequest(int method, String url,
                               Map<String, String> params,
                               Listener<String> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mParams = params;
        setShouldCache(method == Method.GET);
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
    protected Map<String, String> getParams()
            throws AuthFailureError {

        Map<String, String> params = super
                .getParams();

        if (params == null || params.equals(java.util.Collections.emptyMap())) {
            params = new java.util.HashMap<String, String>();
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
    public Map<String, String> getHeaders()
            throws AuthFailureError {
        Map<String, String> headers = super
                .getHeaders();

        if (headers == null || headers.equals(java.util.Collections.emptyMap())) {
            headers = new java.util.HashMap<String, String>();
        }

        CustomVolley.getInstance().addSessionCookie(headers);

        return headers;
    }

    public void setEnableCache(boolean enableCache) {
        setShouldCache(enableCache);
    }

    /**
     * 重写父类方法，手动处理返回值格式，保存cookie。
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        CustomVolley.getInstance().checkSessionCookie(response.headers);

        return formattingData(response);

    }

    private Response<String> formattingData(NetworkResponse response) {

        try {
            String str = new String(response.data, "utf-8");

            BaseEntity baseEntity = JSON.parseObject(str, BaseEntity.class);
            if (baseEntity.getCode().equals("0")) {
                return Response.success(baseEntity.getBody(), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new DataError(baseEntity.getCode(), baseEntity.getMessage()));
            }

        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=" + getParamsEncoding();
    }

    public interface RequestListener {

        void onSuccess(String result);

        // 自定义数据错误
        void onCustomFail(DataError dataError);

        void onFail(VolleyError volleyError);

    }

}
