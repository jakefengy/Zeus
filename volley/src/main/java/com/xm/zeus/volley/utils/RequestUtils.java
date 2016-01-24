package com.xm.zeus.volley.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xm.zeus.common.Tip;
import com.xm.zeus.volley.exception.DataError;
import com.xm.zeus.volley.expand.CustomFileRequest;
import com.xm.zeus.volley.expand.CustomGsonRequest;
import com.xm.zeus.volley.expand.CustomVolley;
import com.xm.zeus.volley.source.DefaultRetryPolicy;
import com.xm.zeus.volley.source.Request.Method;
import com.xm.zeus.volley.source.Response.ErrorListener;
import com.xm.zeus.volley.source.Response.Listener;
import com.xm.zeus.volley.source.VolleyError;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 网络请求入口。使用前，需要在Application中注册RequestUtils组建，注册方法：RequestUtils.initRequestUtils
 * (appContext);
 */
public class RequestUtils {

    private final static String TAG = RequestUtils.class.getName();

    // 请求超时设置
    private static int initialTimeoutMs = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
    private static int maxNumRetries = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

    // 是否打印日志
    private static boolean isPrintLog = false;

    /**
     * 初始化网络组建。使用前必须调用该方法。
     *
     * @param appContext Application级Context.
     */
    public static void initRequestUtils(Context appContext) {
        CustomVolley.initVolleyUtils(appContext);
    }

    /**
     * 判断网络是否可用
     *
     * @return boolean true：联网，false：无网络连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置超时时间，必须在 sendRequest 之前调用（超时默认2500ms）
     *
     * @param timeoutMs 超时时间（毫秒）
     */
    public static void setInitialTimeoutMs(int timeoutMs) {
        if (timeoutMs > -1) {
            initialTimeoutMs = timeoutMs;
        }
    }

    /**
     * 发送请求，返回值格式为自定义类型
     *
     * @param context  上下文，用于网络判断
     * @param tag      请求唯一标识
     * @param method   请求类型，如Get、Post，详情请查看： {@link Method}
     * @param url      网络请求地址
     * @param params   请求参数(HashMap<String, String>)
     * @param type     返回值数据类型。如Custom.class 或 new TypeToken<List<BizTest>>()
     *                 {}.getType()
     * @param callback 请求回调接口
     */
    public static <T> void sendRequest(Context context, String tag, int method,
                                       String url, HashMap<String, String> params, Type type,
                                       RequestListener<T> callback) {
        try {
            if (!isConnected(context)) {
                Tip.toast(context, "网络不可用，请检查网络连接！");
                if (callback != null) {
                    callback.onError(new VolleyError("Net is Disconnected"));
                }
                return;

            }

            if (TextUtils.isEmpty(url)) {
                if (callback != null) {
                    callback.onError(new VolleyError("url is empty"));
                }
                return;
            }

            if (params == null) {
                params = new HashMap<>();
            }

            if (type == null) {
                type = String.class;
            }

            if (TextUtils.isEmpty(tag)) {
                tag = UUID.randomUUID().toString();
            }

            if (method == Method.GET) {
                String realUrl = RequestUtils.getRequestUrl(url, params);
                buildRequest(context, tag, method, realUrl, null, type,
                        callback, initialTimeoutMs, maxNumRetries);
                print("Request.Type = Get , Url = " + realUrl);

            } else if (method == Method.POST) {
                buildRequest(context, tag, method, url, params, type,
                        callback, initialTimeoutMs, maxNumRetries);
                print("Request.Type = Get , Url = " + url + " , and Params is:" + new JSONObject(params).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建新请求
     *
     * @param context  上下文，用于网络判断
     * @param tag      请求唯一标识
     * @param method   请求类型，如Get、Post，详情请查看： {@link Method}
     * @param url      网络请求地址
     * @param params   请求参数(HashMap<String, String>)
     * @param type     返回值数据类型。如Custom.class 或 new TypeToken<List<BizTest>>()
     *                 {}.getType()
     * @param listener 请求回调接口
     */
    private static <T> void buildRequest(Context context, final String tag,
                                         int method, String url, HashMap<String, String> params, Type type,
                                         final RequestListener<T> listener, int timeoutMs, int maxNumRetries) {

        CustomGsonRequest<T> sReq = new CustomGsonRequest<>(method, url, type,
                params, new Listener<T>() {

            @Override
            public void onResponse(T response) {
                print(JSON.toJSONString(response));
                if (listener != null)
                    listener.onSuccess(response);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                print(error.getMessage());
                if (listener != null)
                    listener.onError(error);

            }
        });

        sReq.setRetryPolicy(timeoutMs, maxNumRetries);

        getVolleyUtilsInstance().addToRequestQueue(sReq, tag);
    }

    /**
     * 文件上传，默认Post。
     *
     * @param context  上下文，用于网络判断
     * @param tag      请求唯一标识
     * @param url      网络请求地址
     * @param params   请求参数(HashMap<String, String>)
     * @param files    请求参数(HashMap<String, String>)
     * @param type     返回值数据类型。如Custom.class 或 new TypeToken<List<BizTest>>()
     *                 {}.getType()
     * @param callback 请求回调接口
     */
    public static <T> void sendRequest(Context context, String tag, String url,
                                       Map<String, String> params, Map<String, File> files, Type type,
                                       final RequestListener<T> callback) {

        if (!isConnected(context)) {
            Tip.toast(context, "网络不可用，请检查网络连接！");
            if (callback != null) {
                callback.onError(new VolleyError("Net is Disconnected"));
            }
            return;

        }

        if (TextUtils.isEmpty(url)) {
            if (callback != null) {
                callback.onError(new VolleyError("url is empty"));
            }
            return;
        }

        if (params == null) {
            params = new HashMap<>();
        }

        if (files == null) {
            files = new HashMap<>();
        }

        if (type == null) {
            type = String.class;
        }

        if (TextUtils.isEmpty(tag)) {
            tag = UUID.randomUUID().toString();
        }

        CustomFileRequest<T> fReq = new CustomFileRequest<T>(Method.POST,
                url, type, params, files, new Listener<T>() {

            @Override
            public void onResponse(T response) {
                print(JSON.toJSONString(response));
                if (callback != null)
                    callback.onSuccess(response);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                print(error.getMessage());
                if (callback != null)
                    callback.onError(error);
            }
        });

        getVolleyUtilsInstance().addToRequestQueue(fReq, tag);

    }

    /**
     * 取消当前标签为mTag的请求
     *
     * @param tag
     */
    public static void cancelRequest(String tag) {
        getVolleyUtilsInstance().cancelPendingRequests(tag);
    }

    /**
     * 取消当前标签在mTags中的请求
     *
     * @param tags
     */
    public static void cancelRequest(List<String> tags) {
        for (String tag : tags) {
            cancelRequest(tag);
        }
    }

	/* --------------------------私有方法--------------------------------- */

    /**
     * 生成Get请求真实Url
     *
     * @param strUrl         网络请求地址
     * @param nameValuePairs 参数
     * @return url
     */
    private static String getRequestUrl(String strUrl,
                                        HashMap<String, String> nameValuePairs) {
        try {
            String str = EntityUtils.toString(new UrlEncodedFormEntity(
                    transformToNameValuePair(nameValuePairs), "utf-8"));
            strUrl = strUrl + "?" + str;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUrl;
    }

    private static CustomVolley getVolleyUtilsInstance() {
        return CustomVolley.getInstance();
    }

    /**
     * 将HashMap<String, String>转换成NameValuePair
     *
     * @param params HashMap<String, String> 参数
     * @return
     */
    private static List<NameValuePair> transformToNameValuePair(
            HashMap<String, String> params) {
        List<NameValuePair> mResults = new ArrayList<NameValuePair>();
        if (params != null) {
            Iterator<Entry<String, String>> iterator = params.entrySet()
                    .iterator();
            while (iterator.hasNext()) {

                @SuppressWarnings("rawtypes")
                Map.Entry entry = (Map.Entry) iterator.next();
                NameValuePair mNameValue = new BasicNameValuePair(
                        (String) (entry.getKey()), (String) (entry.getValue()));

                mResults.add(mNameValue);

            }
        }

        return mResults;
    }

    // Interface
    public interface RequestListener<T> {

        void onSuccess(T result);

        void onDataError(DataError dataError);

        void onError(VolleyError volleyError);

    }

    // Log
    private static void print(String msg) {
        if (isPrintLog)
            Log.i(TAG, msg);
    }

}
