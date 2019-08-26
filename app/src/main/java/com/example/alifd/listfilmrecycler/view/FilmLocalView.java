package com.example.alifd.listfilmrecycler.view;

import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.List;

public interface FilmLocalView {
    void onChangeToFavorite(List<FilmModel> filmModels);
    void onChangeToList();
}
