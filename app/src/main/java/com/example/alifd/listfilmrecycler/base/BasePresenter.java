package com.example.alifd.listfilmrecycler.base;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class BasePresenter {

    public String getErrorStatus(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            Timber.e(responseBody.string());
            return jsonObject.getString("status_code");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("status_message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
