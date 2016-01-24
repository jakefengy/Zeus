package com.xm.zeus.volley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.xm.zeus.volley.exception.DataError;
import com.xm.zeus.volley.expand.CustomGsonRequest;
import com.xm.zeus.volley.model.LoginModel;
import com.xm.zeus.volley.source.Request;
import com.xm.zeus.volley.source.VolleyError;
import com.xm.zeus.volley.utils.RequestUtils;

import java.util.HashMap;

/**
 * 测试volley接口
 */
public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        get();

    }

    private void get() {
        String url = "";
        HashMap<String, String> params = new HashMap<>();

        RequestUtils.sendRequest(Test.this, "get", Request.Method.GET, url, params, LoginModel.class,
                new CustomGsonRequest.RequestListener<LoginModel>() {
                    @Override
                    public void onSuccess(LoginModel result) {
                        System.out.println(JSON.toJSONString(result));
                    }

                    @Override
                    public void onDataError(DataError dataError) {
                        dataError.printStackTrace();
                        System.out.println(dataError.toString());
                    }

                    @Override
                    public void onError(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        System.out.println(volleyError.toString());
                    }
                });
    }

    private void post() {

    }

    private void postFile() {

    }

}
