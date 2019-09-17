package com.example.alifd.listfilmrecycler.base;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class BasePresenter {

    public String getErrorStatus(String body) {
        try {
            Timber.e("ini message error status : %s", body);
            JSONObject jsonObject = new JSONObject(body);
            return jsonObject.getString("status_code");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String getErrorMessage(String body) {
        try {
            Timber.e("get error message %s", body);
            JSONObject jsonObject = new JSONObject(body);
            return jsonObject.getString("status_message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
