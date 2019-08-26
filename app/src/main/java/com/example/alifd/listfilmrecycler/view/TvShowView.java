package com.example.alifd.listfilmrecycler.view;

import com.example.alifd.listfilmrecycler.model.TvResponse;

public interface TvShowView {
    void onSuccessGetData(TvResponse tvResponse);
    void onFailed(String code, String message);
}
