package com.example.alifd.listfilmrecycler.view;

import com.example.alifd.listfilmrecycler.model.TvShowModel;

import java.util.List;

public interface TvShowLocalView {
    void onChangeToFavorite(List<TvShowModel> tvShowModels);
    void onChangeToList();
}
