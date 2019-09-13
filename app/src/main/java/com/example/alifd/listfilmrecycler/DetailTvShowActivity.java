package com.example.alifd.listfilmrecycler;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alifd.listfilmrecycler.base.BaseActivity;
import com.example.alifd.listfilmrecycler.model.TvShowModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTvShowActivity extends BaseActivity {

    @BindView(R.id.iv_des_tv)
    ImageView ivDesTv;
    @BindView(R.id.tv_des_tv)
    TextView tvDesTv;

    public static final String DETAIL_TV = "detail_tv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        TvShowModel TvModel = getIntent().getParcelableExtra(DETAIL_TV);

        String imgPath = String.format("%s/%s",sessionManager.getImgBaseUrl(), TvModel.getPosterPath());
        Glide.with(this)
                .load(imgPath)
                .into(ivDesTv);
        Double scoreRaw =TvModel.getVoteAverage()*10.0;
        String score = scoreRaw.intValue()+"%";


        tvDesTv.setText(String.format("%s\n%s\n\nSkor %s\n\nOverview \n%s",TvModel.getName(),
                TvModel.getFirstAirDate(), score, TvModel.getOverview()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idSelected = item.getItemId();

        switch (idSelected) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
