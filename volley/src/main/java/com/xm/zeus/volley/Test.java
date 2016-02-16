package com.xm.zeus.volley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.xm.zeus.volley.entity.LoginEntity;
import com.xm.zeus.volley.exception.DataError;
import com.xm.zeus.volley.expand.CustomStringRequest;
import com.xm.zeus.volley.expand.RequestCallbackCode;
import com.xm.zeus.volley.source.VolleyError;
import com.xm.zeus.volley.utils.RequestUtils;

import java.util.HashMap;

/**
 * 测试volley接口
 */
public class Test extends AppCompatActivity {

    private abstract class AbstractRequestListener implements CustomStringRequest.RequestListener {

        @Override
        public void onCustomFail(DataError dataError) {
            if (dataError.getErrorCode().equals(RequestCallbackCode.NetworkUnavailable.getCode())) {

            }
        }

        @Override
        public void onFail(VolleyError volleyError) {

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        get();

    }

    private void get() {
        String url = "";
        HashMap<String, String> params = new HashMap<>();

        CustomStringRequest.RequestListener listener = new AbstractRequestListener() {
            @Override
            public void onSuccess(String result) {
                LoginEntity loginEntity = JSON.parseObject(result, LoginEntity.class);
            }
        };

        RequestUtils.sendStringRequest(Test.this, "sendStringRequest", url, params, listener);
    }

    private void post() {

    }

    private void postFile() {

    }

}
