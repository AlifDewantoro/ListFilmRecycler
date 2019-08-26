package com.example.alifd.listfilmrecycler;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alifd.listfilmrecycler.adapter.CustomPagerAdapter;
import com.example.alifd.listfilmrecycler.base.BaseActivity;
import com.example.alifd.listfilmrecycler.helper.RealmManager;
import com.example.alifd.listfilmrecycler.view.FilmLocalView;
import com.example.alifd.listfilmrecycler.view.TvShowLocalView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends BaseActivity {

    @BindView(R.id.film_tabLayout)
    TabLayout filmTabLayout;
    @BindView(R.id.film_viewPager)
    ViewPager filmViewPager;

    MenuItem favorite;
    boolean favVisibility;
    MenuItem allList;
    boolean listVisibility;
    MenuItem setLanguage;
    private RealmManager realmManager;

    FilmLocalView filmLocalView;
    TvShowLocalView tvShowLocalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        realmManager = new RealmManager(realm);

        if(savedInstanceState!=null){
            favVisibility = savedInstanceState.getBoolean("menu_fav",true);
            listVisibility = savedInstanceState.getBoolean("menu_list", false);
        }else{
            favVisibility = true;
            listVisibility = false;
        }

        TabLayout.Tab firstTab = filmTabLayout.newTab();
        firstTab.setText(getString(R.string.film_menu));
        TabLayout.Tab secondTab = filmTabLayout.newTab();
        secondTab.setText(getString(R.string.tvshow_menu));
        filmTabLayout.addTab(firstTab);
        filmTabLayout.addTab(secondTab);

        filmViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(filmTabLayout));

        filmTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filmViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), filmTabLayout.getTabCount());
        filmViewPager.setAdapter(pagerAdapter);
        filmViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(filmTabLayout));

    }

    public void setFilmFragInteractor(FilmLocalView filmLocalView){
        this.filmLocalView = filmLocalView;
    }

    public void setTvShowFragInteractor(TvShowLocalView tvShowLocalView){
        this.tvShowLocalView = tvShowLocalView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        favorite = menu.findItem(R.id.action_to_favorite);
        allList = menu.findItem(R.id.action_to_list);
        favorite.setVisible(favVisibility);
        allList.setVisible(listVisibility);
        setLanguage = menu.findItem(R.id.action_change_settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case (R.id.action_change_settings):
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case(R.id.action_to_favorite):
                listVisibility = true;
                favVisibility = false;
                allList.setVisible(listVisibility);
                favorite.setVisible(favVisibility);
                filmLocalView.onChangeToFavorite(realmManager.getListFilmFav());
                tvShowLocalView.onChangeToFavorite(realmManager.getListTvShowFav());
                break;
            case(R.id.action_to_list):
                favVisibility = true;
                listVisibility = false;
                favorite.setVisible(favVisibility);
                allList.setVisible(listVisibility);
                filmLocalView.onChangeToList();
                tvShowLocalView.onChangeToList();
                break;
            default:
                break;

        }
        if (item.getItemId() == R.id.action_change_settings){
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("menu_fav", favVisibility);
        outState.putBoolean("menu_list", listVisibility);
        super.onSaveInstanceState(outState);
    }
}
