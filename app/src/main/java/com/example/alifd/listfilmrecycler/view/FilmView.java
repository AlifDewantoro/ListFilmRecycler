package com.example.alifd.listfilmrecycler.view;

import com.example.alifd.listfilmrecycler.model.FilmResponse;

public interface FilmView {
    void onSuccessGetData(FilmResponse filmResponse);
    void onFailed(String code, String message);
}
