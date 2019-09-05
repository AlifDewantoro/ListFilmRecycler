package com.example.alifd.listfilmrecycler.view;

import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.ArrayList;

public interface LoadFilmsCallback {
    void preExecute();
    void postExecute(ArrayList<FilmModel> filmModels);
}
