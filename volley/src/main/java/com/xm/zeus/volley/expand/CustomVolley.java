package com.xm.zeus.volley.expand;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import java.util.Map;

import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.RequestQueue;
import com.xm.zeus.volley.source.VolleyLog;
import com.xm.zeus.volley.source.toolbox.HttpStack;
import com.xm.zeus.volley.source.toolbox.Volley;

/**
 * 单独管理Volley的事务。
 */
public class CustomVolley {

    private static CustomVolley mInstance;
    private static Context mAppContext;

    public static final String TAG = "VolleyPatterns";

    private static final String SET_COOKIE_KEY = "Set-Cookie";

    private static final String COOKIE_KEY = "Cookie";

    private RequestQueue mRequestQueue;

    /**
     * 初始化Volley组建
     *
     * @param appContext
     */
    public static void initVolleyUtils(Context appContext) {
        mAppContext = appContext;
    }

    /**
     * 获取CustomVolley对象
     *
     * @return new CustomVolley()
     */
    public synchronized static CustomVolley getInstance() {

        if (mAppContext == null) {
            throw new NullPointerException(
                    "mAppContext can't be null. Please Calling RequestUtils.initRequestUtils(appContext) first !");
        }

        if (mInstance == null) {
            mInstance = new CustomVolley();
        }

        return mInstance;
    }

    /**
     * @return Volley 查询队列
     */
    public RequestQueue getRequestQueue(String tag) {

        if (mRequestQueue == null) {

            Log.i("RequestQueue Initialize",
                    "mRequestQueue is Created by getRequestQueue()  When  "
                            + tag);

            DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();

            ClientConnectionManager mClientConnectionManager = mDefaultHttpClient
                    .getConnectionManager();
            HttpParams mHttpParams = mDefaultHttpClient.getParams();
            ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager(
                    mHttpParams, mClientConnectionManager.getSchemeRegistry());

            mDefaultHttpClient = new DefaultHttpClient(
                    mThreadSafeClientConnManager, mHttpParams);

            HttpStack httpStack = new CustomHttpStack(mDefaultHttpClient);

            mRequestQueue = Volley.newRequestQueue(mAppContext, httpStack);

        }

        return mRequestQueue;
    }

    /**
     * 添加Request到Volley队列中，使用自定义标记
     *
     * @param req
     * @param tag 标记request
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue(tag).add(req);

    }

    /**
     * 添加Request到Volley队列中，使用默认标记
     *
     * @param req
     */
    public static <T> void addToRequestQueue(Request<T> req) {

        req.setTag(TAG);

        mInstance.getRequestQueue(TAG).add(req);
    }

    /**
     * 根据tag停止request
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public RequestQueue getRequestQueue() {
        return getRequestQueue("");
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    /**
     * 保存cookies
     *
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        // && headers.sendStringRequest(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)
        if (headers.containsKey(SET_COOKIE_KEY)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                // String[] splitCookie = cookie.split(";");
                // String[] splitSessionId = splitCookie[0].split("=");
                // cookie = splitSessionId[1];
//				mSharePreferences.setValue(COOKIE_KEY, cookie);
            }
        }
    }

    /**
     * 添加cookies
     *
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
//		String sessionId = mSharePreferences.getValue(COOKIE_KEY, "");
//		if (sessionId.length() > 0) {
//			// StringBuilder builder = new StringBuilder();
//			// builder.append(SESSION_COOKIE);
//			// builder.append("=");
//			// builder.append(sessionId);
//			// if (headers.containsKey(COOKIE_KEY)) {
//			// builder.append("; ");
//			// builder.append(headers.sendStringRequest(COOKIE_KEY));
//			// }
//			headers.put(SET_COOKIE_KEY, sessionId);
//		}
    }

}
