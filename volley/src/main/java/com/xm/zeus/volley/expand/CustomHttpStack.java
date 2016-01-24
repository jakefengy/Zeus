package com.xm.zeus.volley.expand;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

import com.xm.zeus.volley.source.AuthFailureError;
import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.Request.Method;
import com.xm.zeus.volley.source.toolbox.HttpStack;

/**
 * CustomHttpStack -
 * 自定义HttpStack，实现Content-Type为multipart/form-data和application/
 * x-www-form-urlencoded
 */
public class CustomHttpStack implements HttpStack {
    protected final HttpClient mClient;

    private final static java.lang.String HEADER_CONTENT_TYPE = "Content-Type";

    public CustomHttpStack(HttpClient client) {
        mClient = client;
    }

    /**
     * 添加头文件信息
     *
     * @param httpRequest Http请求对象
     * @param headers     头文件信息
     */
    private static void addHeaders(HttpUriRequest httpRequest,
                                   java.util.Map<java.lang.String, java.lang.String> headers) {
        for (java.lang.String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    @SuppressWarnings("unused")
    private static java.util.List<NameValuePair> getPostParameterPairs(
            java.util.Map<java.lang.String, java.lang.String> postParams) {
        java.util.List<NameValuePair> result = new java.util.ArrayList<NameValuePair>(
                postParams.size());
        for (java.lang.String key : postParams.keySet()) {
            result.add(new BasicNameValuePair(key, postParams.get(key)));
        }
        return result;
    }

    @Override
    public HttpResponse performRequest(Request<?> request,
                                       java.util.Map<java.lang.String, java.lang.String> additionalHeaders)
            throws java.io.IOException, AuthFailureError {
        HttpUriRequest httpRequest = createHttpRequest(request,
                additionalHeaders);
        addHeaders(httpRequest, additionalHeaders);
        addHeaders(httpRequest, request.getHeaders());
        onPrepareRequest(httpRequest);
        HttpParams httpParams = httpRequest.getParams();
        int timeoutMs = request.getTimeoutMs();
        // TODO: Reevaluate this connection timeout based on more wide-scale
        // data collection and possibly different for wifi vs. 3G.
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        return mClient.execute(httpRequest);
    }

    @SuppressWarnings("deprecation")
    /* protected */ static HttpUriRequest createHttpRequest(Request<?> request,
                                                            java.util.Map<java.lang.String, java.lang.String> additionalHeaders)
            throws AuthFailureError, UnsupportedEncodingException {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST: {
                // This is the deprecated way that needs to be handled for backwards
                // compatibility.
                // If the request's post body is null, then the assumption is that
                // the request is
                // GET. Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    HttpPost postRequest = new HttpPost(request.getUrl());
                    postRequest.addHeader(HEADER_CONTENT_TYPE,
                            request.getPostBodyContentType());
                    HttpEntity entity;
                    entity = new ByteArrayEntity(postBody);
                    postRequest.setEntity(entity);
                    return postRequest;
                } else {
                    return new HttpGet(request.getUrl());
                }
            }
            case Method.GET:
                return new HttpGet(request.getUrl());
            case Method.DELETE:
                return new HttpDelete(request.getUrl());
            case Method.POST: {
                HttpPost postRequest = new HttpPost(request.getUrl());
                if (request.getBodyContentType() != null) {
                    postRequest.addHeader(HEADER_CONTENT_TYPE,
                            request.getBodyContentType());
                }
                setEntityIfNonEmptyBody(postRequest, request);
                return postRequest;
            }
            case Method.PUT: {
                HttpPut putRequest = new HttpPut(request.getUrl());
                if (request.getBodyContentType() != null) {
                    putRequest.addHeader(HEADER_CONTENT_TYPE,
                            request.getBodyContentType());
                }
                setEntityIfNonEmptyBody(putRequest, request);
                return putRequest;
            }
            case Method.HEAD:
                return new HttpHead(request.getUrl());
            case Method.OPTIONS:
                return new HttpOptions(request.getUrl());
            case Method.TRACE:
                return new HttpTrace(request.getUrl());
            case Method.PATCH: {
                HttpPatch patchRequest = new HttpPatch(request.getUrl());
                patchRequest.addHeader(HEADER_CONTENT_TYPE,
                        request.getBodyContentType());
                setEntityIfNonEmptyBody(patchRequest, request);
                return patchRequest;
            }
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    /**
     * 若该Request为CustomMultiPartStringRequest，则设置他的头文件信息Content-Type为multipart/
     * form-data否则application/* x-www-form-urlencoded
     *
     * @param httpRequest
     * @param request
     * @throws AuthFailureError
     */
    public static void setEntityIfNonEmptyBody(
            HttpEntityEnclosingRequestBase httpRequest, Request<?> request)
            throws AuthFailureError, UnsupportedEncodingException {

        if (request instanceof IMultiPartRequest) {

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE);

            java.util.Map<java.lang.String, java.io.File> fileUpload = ((IMultiPartRequest) request)
                    .getFileUploads();
            for (java.util.Map.Entry<java.lang.String, java.io.File> entry : fileUpload
                    .entrySet()) {

                builder.addPart(((java.lang.String) entry.getKey()),
                        new FileBody((java.io.File) entry.getValue()));
            }

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE,
                    HTTP.UTF_8);

            java.util.Map<java.lang.String, java.lang.String> stringUpload = ((IMultiPartRequest) request)
                    .getStringUploads();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : stringUpload
                    .entrySet()) {
                try {
                    builder.addPart(((java.lang.String) entry.getKey()),
                            new org.apache.http.entity.mime.content.StringBody(
                                    (java.lang.String) entry.getValue(),
                                    contentType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            httpRequest.setEntity(builder.build());

        } else {

            byte[] body = request.getBody();

            HttpEntity entity = new ByteArrayEntity(body);
            httpRequest.setEntity(entity);

//            StringEntity strEntity = new StringEntity(new String(body, HTTP.UTF_8), HTTP.UTF_8);
//            httpRequest.setEntity(strEntity);

        }

    }

    /**
     * 在请求执行前被调用
     *
     * @hide Called before the request is executed using the underlying
     * HttpClient.
     * <p/>
     * <p>
     * Overwrite in subclasses to augment the request.
     * </p>
     */
    protected void onPrepareRequest(HttpUriRequest request)
            throws java.io.IOException {
        // Nothing.
    }

    /**
     * The HttpPatch class does not exist in the Android framework, so this has
     * been defined here. HttpPatch类在Android框架中不存在
     */
    public static final class HttpPatch extends HttpEntityEnclosingRequestBase {

        public final static java.lang.String METHOD_NAME = "PATCH";

        public HttpPatch() {
            super();
        }

        public HttpPatch(final java.net.URI uri) {
            super();
            setURI(uri);
        }

        /**
         * @throws IllegalArgumentException if the uri is invalid.
         */
        public HttpPatch(final java.lang.String uri) {
            super();
            setURI(java.net.URI.create(uri));
        }

        @Override
        public java.lang.String getMethod() {
            return METHOD_NAME;
        }

    }
}
