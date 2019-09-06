package com.example.alifd.listfilmrecycler.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alifd.listfilmrecycler.BuildConfig;
import com.example.alifd.listfilmrecycler.MainActivity;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.adapter.TvShowAdapter;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.TvResponse;
import com.example.alifd.listfilmrecycler.model.TvShowModel;
import com.example.alifd.listfilmrecycler.presenter.TvShowPresenter;
import com.example.alifd.listfilmrecycler.view.TvShowLocalView;
import com.example.alifd.listfilmrecycler.view.TvShowView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements TvShowView, TvShowLocalView {

    RecyclerView rvTv;
    SessionManager sessionManager;
    ProgressBar progressBar;
    SearchView searchTv;
    SwipeRefreshLayout refresh;

    TvShowPresenter tvShowPresenter;
    TvShowAdapter tvShowAdapter;

    ArrayList<TvShowModel> tvShowModelArrayList;
    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        rvTv = view.findViewById(R.id.rv_tv_show);
        progressBar = view.findViewById(R.id.progress_bar);
        searchTv = view.findViewById(R.id.search_tv);
        refresh = view.findViewById(R.id.swipe_show);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        if(getActivity()!=null){
            ((MainActivity)getActivity()).setTvShowFragInteractor(this);
        }

        tvShowPresenter = new TvShowPresenter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvTv.setLayoutManager(layoutManager);

        if(tvShowModelArrayList==null){
            tvShowModelArrayList= new ArrayList<>();
        }

        if(savedInstanceState!=null){
            tvShowModelArrayList = savedInstanceState.getParcelableArrayList("data_tv");
            tvShowAdapter = new TvShowAdapter(getContext(), tvShowModelArrayList);
            rvTv.setAdapter(tvShowAdapter);
            progressBar.setVisibility(View.GONE);
        }else {
            requestData();
        }

        searchTv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.e("sum search char : %s", query.length());
                if(query.length()>0) {
                    Timber.e("search");
                    tvShowModelArrayList.clear();
                    tvShowPresenter.getTvListByQuery(BuildConfig.API_KEY, sessionManager.getLanguage(), query);
                    rvTv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                searchTv.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                /*
                if(query.length()==0){
                    Timber.e("normal");
                    tvShowModelArrayList.clear();
                    requestData();
                }
                 */
                return false;
            }
        });
        return view;
    }

    private void requestData(){
        if(getContext()!=null) {
            sessionManager = new SessionManager(getContext());
        }else {
            Timber.e("context null");
        }
        tvShowPresenter.getTvList(BuildConfig.API_KEY, sessionManager.getLanguage());
        rvTv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetData(TvResponse tvResponse) {
        progressBar.setVisibility(View.GONE);
        rvTv.setVisibility(View.VISIBLE);
        tvShowModelArrayList.addAll(tvResponse.getResults());
        if(tvShowAdapter==null) {
            tvShowAdapter = new TvShowAdapter(getContext(), tvShowModelArrayList);
            rvTv.setAdapter(tvShowAdapter);
        }else{
            tvShowAdapter.notifyDataSetChanged();
        }

        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
            tvShowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed(String code, String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("data_tv", tvShowModelArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onChangeToFavorite(List<TvShowModel> tvShowModels) {
        Timber.e("to fav");
        if(tvShowModelArrayList!=null) {
            tvShowModelArrayList.clear();
            tvShowModelArrayList.addAll(tvShowModels);
            tvShowAdapter.notifyDataSetChanged();
        }
        searchTv.setVisibility(View.GONE);
    }

    @Override
    public void onChangeToList() {
        Timber.e("to list");
        tvShowModelArrayList.clear();
        searchTv.setQuery("",true);
        searchTv.clearFocus();
        requestData();
        searchTv.setVisibility(View.VISIBLE);
    }
}
