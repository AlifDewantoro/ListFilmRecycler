package com.example.alifd.listfilmrecycler.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.alifd.listfilmrecycler.BuildConfig;
import com.example.alifd.listfilmrecycler.MainActivity;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.adapter.FilmAdapter;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.FilmModel;
import com.example.alifd.listfilmrecycler.model.FilmResponse;
import com.example.alifd.listfilmrecycler.presenter.FilmPresenter;
import com.example.alifd.listfilmrecycler.view.FilmLocalView;
import com.example.alifd.listfilmrecycler.view.FilmView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class FilmFragment extends Fragment implements FilmView, FilmLocalView {

    RecyclerView rvFilm;
    SessionManager sessionManager;
    ProgressBar progressBar;
    SearchView searchFilm;
    SwipeRefreshLayout refresh;

    FilmPresenter filmPresenter;
    FilmAdapter filmAdapter;

    ArrayList<FilmModel> filmModelArrayList;

    public FilmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);

        rvFilm = view.findViewById(R.id.rv_film);
        progressBar = view.findViewById(R.id.progress_bar);
        searchFilm = view.findViewById(R.id.search_film);
        refresh = view.findViewById(R.id.swipe_film);

        if(getActivity()!=null){
            ((MainActivity)getActivity()).setFilmFragInteractor(this);
        }

        filmPresenter = new FilmPresenter(this);
        int coloum = calculateNoOfColumns(Objects.requireNonNull(getContext()), 180f);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), coloum);
        rvFilm.setLayoutManager(layoutManager);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filmAdapter.doRefreshFilmDataDb();
                requestData();
            }
        });

        if(filmModelArrayList==null){
            filmModelArrayList= new ArrayList<>();
        }
        if(savedInstanceState!=null){
            Timber.e("masuk savedInstance");
            filmModelArrayList = savedInstanceState.getParcelableArrayList("data");
            filmAdapter = new FilmAdapter(getContext(), filmModelArrayList);
            rvFilm.setAdapter(filmAdapter);
            progressBar.setVisibility(View.GONE);
        }else {
            Timber.e("request normal");
            requestData();
        }
        setSearchNormal();
        return view;
    }

    private void setSearchNormal(){
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.e("sum search char : %s", query.length());
                if(query.length()>0) {
                    Timber.e("search");
                    filmModelArrayList.clear();
                    filmPresenter.getFilmListByQuery(BuildConfig.API_KEY, sessionManager.getLanguage(), query);
                    rvFilm.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                searchFilm.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                /*
                if(query.length()==0){
                    Timber.e("normal");
                    filmModelArrayList.clear();
                    requestData();
                }
                 */
                return false;
            }
        });
    }

    private void requestData(){
        if(getContext()!=null) {
            sessionManager = new SessionManager(getContext());
        }else {
            Timber.e("context null");
        }
        filmPresenter.getFilmList(BuildConfig.API_KEY, sessionManager.getLanguage());
        Timber.e("get key %s", BuildConfig.API_KEY);
        rvFilm.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetData(FilmResponse filmResponse) {
        progressBar.setVisibility(View.GONE);
        rvFilm.setVisibility(View.VISIBLE);
        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
            filmModelArrayList.clear();
            filmModelArrayList.addAll(filmResponse.getResults());
        }else{
            filmModelArrayList.addAll(filmResponse.getResults());
        }
        if(filmAdapter==null) {
            filmAdapter = new FilmAdapter(getContext(), filmModelArrayList);
            rvFilm.setAdapter(filmAdapter);
        }else{
            filmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed(String code, String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        Timber.e(message);
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("data", filmModelArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onChangeToFavorite(List<FilmModel> filmModels) {
        Timber.e("to fav");
        if(filmModelArrayList!=null) {
            filmModelArrayList.clear();
            filmModelArrayList.addAll(filmModels);
            filmAdapter.notifyDataSetChanged();
        }
        searchFilm.setVisibility(View.GONE);
    }

    @Override
    public void onChangeToList() {
        Timber.e("to list");
        filmModelArrayList.clear();
        searchFilm.setQuery("",true);
        searchFilm.clearFocus();
        requestData();
        searchFilm.setVisibility(View.VISIBLE);
    }
}
