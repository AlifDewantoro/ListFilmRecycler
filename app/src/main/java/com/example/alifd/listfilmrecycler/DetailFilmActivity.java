package com.example.alifd.listfilmrecycler;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alifd.listfilmrecycler.base.BaseActivity;
import com.example.alifd.listfilmrecycler.model.FilmModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailFilmActivity extends BaseActivity {


    @BindView(R.id.iv_des_film)
    ImageView iv_des_film;
    @BindView(R.id.tv_des_desc)
    TextView tv_des_desc;

    public static final String DETAIL_FILM = "detail_film";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        FilmModel filmModel = getIntent().getParcelableExtra(DETAIL_FILM);

        String imgPath = String.format("%s/%s",sessionManager.getImgBaseUrl(), filmModel.getPosterPath());
        Glide.with(this)
                .load(imgPath)
                .into(iv_des_film);
        Double scoreRaw =filmModel.getVoteAverage()*10.0;
        String score = scoreRaw.intValue()+"%";


        tv_des_desc.setText(String.format("%s\n%s\n\nSkor %s\n\nOverview \n%s",filmModel.getTitle(),
                filmModel.getReleaseDate(), score, filmModel.getOverview()));
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
