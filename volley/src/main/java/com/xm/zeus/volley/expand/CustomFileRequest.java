package com.xm.zeus.volley.expand;


import com.alibaba.fastjson.JSON;
import com.xm.zeus.volley.source.AuthFailureError;
import com.xm.zeus.volley.source.NetworkResponse;
import com.xm.zeus.volley.source.ParseError;
import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.Response;
import com.xm.zeus.volley.source.toolbox.HttpHeaderParser;


/**
 * 自定义Request，添加Cookie管理，实现文件上传。返回值类型为String
 *
 * @param <T>
 */
public class CustomFileRequest<T> extends Request<T> implements IMultiPartRequest {

    /**
     * 需要转化成的Gson类型
     */
    private final java.lang.reflect.Type mType;

    /**
     * 请求回调接口
     */
    private final Response.Listener<T> mListener;

    /**
     * 存放上传文件的集合
     */
    private java.util.Map<java.lang.String, java.io.File> fileUploads = new java.util.HashMap<java.lang.String, java.io.File>();

    /**
     * 存放上传参数的集合
     */
    private java.util.Map<java.lang.String, java.lang.String> stringUploads = new java.util.HashMap<java.lang.String, java.lang.String>();

    /**
     * 创建新请求
     *
     * @param method        请求类型，如Get、Post，详情请查看： {@link Method}
     * @param url           请求地址
     * @param listener      请求成功回调接口
     * @param errorListener 请求失败回调接口
     */
    public CustomFileRequest(int method, java.lang.String url,
                             java.lang.reflect.Type type, java.util.Map<String, String> params,
                             java.util.Map<String, java.io.File> files, Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mType = type;
        stringUploads.putAll(params);
        fileUploads.putAll(files);

    }

    /**
     * 向文件集合中添加文件
     */
    public void addFileUpload(java.lang.String param, java.io.File file) {
        fileUploads.put(param, file);
    }

    /**
     * 向参数集合中添加参数
     */
    public void addStringUpload(java.lang.String param, java.lang.String content) {
        stringUploads.put(param, content);
    }

    /**
     * 要上传的文件
     */
    public java.util.Map<java.lang.String, java.io.File> getFileUploads() {
        return fileUploads;
    }

    /**
     * 要上传的参数
     */
    public java.util.Map<java.lang.String, java.lang.String> getStringUploads() {
        return stringUploads;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            CustomVolley.getInstance().checkSessionCookie(response.headers);

            String json = new java.lang.String(response.data, "utf-8");

            return (Response<T>) Response.success(
                    JSON.parseObject(json, mType),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (java.io.UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

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

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    /**
     * 空表示不上传
     */
    public java.lang.String getBodyContentType() {
        return null;
    }
}