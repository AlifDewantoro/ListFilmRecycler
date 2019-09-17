package com.example.alifd.listfilmrecycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.viewpager.widget.ViewPager;

import com.example.alifd.listfilmrecycler.adapter.CustomPagerAdapter;
import com.example.alifd.listfilmrecycler.base.BaseActivity;
import com.example.alifd.listfilmrecycler.db.FavHelper;
import com.example.alifd.listfilmrecycler.reminder.AlarmReceiver;
import com.example.alifd.listfilmrecycler.reminder.setting.SettingActivity;
import com.example.alifd.listfilmrecycler.view.FilmLocalView;
import com.example.alifd.listfilmrecycler.view.TvShowLocalView;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.alifd.listfilmrecycler.reminder.AlarmReceiver.DAILY_REMINDER;
import static com.example.alifd.listfilmrecycler.reminder.AlarmReceiver.NEW_FILMS;

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
    private FavHelper favHelper;

    FilmLocalView filmLocalView;
    TvShowLocalView tvShowLocalView;

    AlarmReceiver alarmReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        favHelper = FavHelper.getInstance(getApplicationContext());
        favHelper.open();

        if(savedInstanceState!=null){
            favVisibility = savedInstanceState.getBoolean("menu_fav",true);
            listVisibility = savedInstanceState.getBoolean("menu_list", false);
        }else{
            favVisibility = true;
            listVisibility = false;
        }

        alarmReceiver = new AlarmReceiver();

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

        Timber.e("data dari notif di main act : %s", getIntent().getStringExtra("from_notif"));


        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dailySwitch = sh.getBoolean(getResources().getString(R.string.key_daily_notif), false);
        boolean releaseSwitch = sh.getBoolean(getResources().getString(R.string.key_release_notif), false);
        Timber.e("daily setting %s", dailySwitch);
        Timber.e("release setting %s", releaseSwitch);

        if(dailySwitch) {
            alarmReceiver.setDailyReminderNotif(this, DAILY_REMINDER, "Mau nonton film? lihat dlu yuk list filmnya");
        }else {
            alarmReceiver.stopNotification(this, DAILY_REMINDER);
        }

        if(dailySwitch) {
            alarmReceiver.setNewReleaseNotif(this, NEW_FILMS, "Ada film baru nih");
        }else {
            alarmReceiver.stopNotification(this, NEW_FILMS);
        }
    }

    public void setFilmFragInteractor(FilmLocalView filmLocalView){
        this.filmLocalView = filmLocalView;
        Timber.e("status visibility menu %s", favVisibility);
        if(!favVisibility){
            Timber.e("masuk fav");
            filmLocalView.onChangeToFavorite(favHelper.getAllFilmFavs());
        }
    }

    public void setTvShowFragInteractor(TvShowLocalView tvShowLocalView){
        this.tvShowLocalView = tvShowLocalView;
        if(!favVisibility){
            Timber.e("masuk fav");
            tvShowLocalView.onChangeToFavorite(favHelper.getAllShowFavs());
        }
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
                Intent langIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(langIntent);
                break;
            case(R.id.action_to_favorite):
                listVisibility = true;
                favVisibility = false;
                allList.setVisible(listVisibility);
                favorite.setVisible(favVisibility);
                filmLocalView.onChangeToFavorite(favHelper.getAllFilmFavs());
                tvShowLocalView.onChangeToFavorite(favHelper.getAllShowFavs());
                break;
            case(R.id.action_to_list):
                favVisibility = true;
                listVisibility = false;
                favorite.setVisible(favVisibility);
                allList.setVisible(listVisibility);
                filmLocalView.onChangeToList();
                tvShowLocalView.onChangeToList();
                break;
            case(R.id.action_notification_settings):
                Intent notifIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(notifIntent);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("menu_fav", favVisibility);
        outState.putBoolean("menu_list", listVisibility);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favHelper.close();
    }
}
