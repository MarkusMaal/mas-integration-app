package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ComponentCaller;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;
import java.util.function.Consumer;

import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.Edition;
import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;
import ee.mas.integratsioonitarkvara.services.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static CommonConfig config;
    private static Edition edition;
    public static MarkuStationConfig markuStationConfig;

    public static MarkuStationGame[] markuStationGames;
    private static DesktopLayout desktopLayout;

    private boolean firstLoad = true;

    private String auth = "";

    private static ApiService apiService;

    protected enum Tabs {
        WELCOME,
        MARKUSTATION,
        CONFIG,
        DESKTOP,
        ABOUT
    }


    @Override
    public void onNewIntent(@NonNull Intent intent, @NonNull ComponentCaller caller) {
        refresh();
        super.onNewIntent(intent, caller);
    }

    @Override
    protected void onRestart() {
        refresh();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(ContextCompat.getColor(this, R.color.purple_700)));
        setContentView(R.layout.activity_main);OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Auth", getSettingsAuth()).build();
            return chain.proceed(request);
        });
        apiService = new Retrofit.Builder()
                .baseUrl(getSettingsUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(ApiService.class);

        refresh();
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.pager);
        TabCollectionAdapter tabCollectionAdapter = new TabCollectionAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(tabCollectionAdapter);
        //viewPager.setUserInputEnabled(false);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (Tabs.values()[position]) {
                case WELCOME:
                    tab.setText("Uudised");
                    break;
                case MARKUSTATION:
                    tab.setText("MarkuStation");
                    break;
                case CONFIG:
                    tab.setText("Konfiguratsioon");
                    break;
                case DESKTOP:
                    tab.setText("Töölaud");
                    break;
                case ABOUT:
                    tab.setText("Teave");
                    break;
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                var v = getSupportFragmentManager().findFragmentByTag("f" + position);
                if (v instanceof AboutFragment) {
                    refresh(Tabs.ABOUT);
                    ((AboutFragment)v).updateEdition(edition, v.getView());
                } else if (v instanceof ConfigFragment) {
                    refresh(Tabs.CONFIG);
                    ((ConfigFragment)v).updateConfig(config, v.getView());
                } else if (v instanceof MarkuStationFragment) {
                    refresh(Tabs.MARKUSTATION);
                    ((MarkuStationFragment)v).updateMarkuStation(markuStationConfig, markuStationGames, v.getView());
                } else if (v instanceof DesktopFragment) {
                    refresh(Tabs.DESKTOP);
                    ((DesktopFragment)v).updateDesktop(desktopLayout, v.getView());
                }
                super.onPageSelected(position);
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
    }


    public static class TabCollectionAdapter extends FragmentStateAdapter {
        public TabCollectionAdapter(FragmentManager fragment, Lifecycle lc) {
            super(fragment, lc);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (Tabs.values()[position]) {
                case WELCOME:
                    return new Welcome();
                case MARKUSTATION:
                    return new MarkuStationFragment();
                case CONFIG:
                    return new ConfigFragment();
                case DESKTOP:
                    return new DesktopFragment(desktopLayout);
                case ABOUT:
                    return new AboutFragment(edition);
            }
            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return Tabs.values().length;
        }
    }


    public void refresh() {
        refresh(Tabs.ABOUT);
        refresh(Tabs.CONFIG);
        refresh(Tabs.MARKUSTATION);
        refresh(Tabs.DESKTOP);
    }

    public void refresh(Tabs tab) {
        findViewById(R.id.progressBar).setVisibility(VISIBLE);
        switch (tab) {
            case ABOUT:
                enqueue(apiService.getEdition(), e -> edition = e, this);
                break;
            case MARKUSTATION:
                enqueue(apiService.getMarkuStationConfig(), c -> markuStationConfig = c, this);
                enqueue(apiService.getMarkuStationGames(), g -> markuStationGames = g, this);
                break;
            case CONFIG:
                enqueue(apiService.getConfig(), c -> config = c, this);
                break;
            case DESKTOP:
                enqueue(apiService.getDesktopLayout(), d -> desktopLayout = d, this);
                break;
        }
        firstLoad = false;
    }

    private <T> void enqueue(Call<T> call, Consumer<T> onSuccess, Context context) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call,
                                   @NonNull Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    findViewById(R.id.progressBar).setVisibility(GONE);
                    onSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call,
                                  @NonNull Throwable t) {
                Log.println(Log.ERROR, "API request failed", Objects.requireNonNull(t.getMessage()));
                if (!firstLoad) {
                    findViewById(R.id.progressBar).setVisibility(GONE);
                }
                onSuccess.accept(null);
            }
        });
    }

    public void refresh(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private String getSettingsUrl() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        var ip = sp.getString("ip", "192.168.1.201");
        var port = sp.getString("port", "14415");
        return "http://" + ip + ":" + port;
    }

    private String getSettingsAuth() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        auth = sp.getString("code", "");
        return auth;
    }

    public void appSettings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public static void saveConfig(Tabs tab) {
        switch (tab) {
            case CONFIG:
                Call<CommonConfig> call = apiService.saveCommonConfig(config);
                call.enqueue(new CommonConfigCallback());
                break;
            case MARKUSTATION:
                Call<MarkuStationConfig> call2 = apiService.saveMarkuStationConfig(markuStationConfig);
                call2.enqueue(new MarkuStationConfigCallback());
                break;
        }
    }

    public void logoCheckboxClick(View view) {
        if (view instanceof CheckBox) {
            var cb = (CheckBox)view;
            if (cb.getId() == R.id.logoCheckbox) config.setShowLogo(cb.isChecked());
            else if (cb.getId() == R.id.scheduleCheckbox) config.setAllowScheduledTasks(cb.isChecked());
            else if (cb.getId() == R.id.desktopNotesCheckbox) config.setAutostartNotes(cb.isChecked());
        }
        MainActivity.saveConfig(Tabs.CONFIG);
    }


    public void markuStationCheckboxClick(View view) {
        if (view instanceof CheckBox) {
            var cb = (CheckBox)view;
            if (cb.getId() == R.id.introCheckbox) markuStationConfig.setPlayIntro(cb.isChecked());
            else if (cb.getId() == R.id.creepypastaCheckbox) markuStationConfig.setCreepypastaIntro(cb.isChecked());
            else if (cb.getId() == R.id.legacyCheckbox) markuStationConfig.setLegacyIntro(cb.isChecked());
            else if (cb.getId() == R.id.specialCheckbox) markuStationConfig.setSpecialIntro(cb.isChecked());
        }
        MainActivity.saveConfig(Tabs.MARKUSTATION);
    }

    private static class CommonConfigCallback implements Callback<CommonConfig> {
        @Override
        public void onResponse(@NonNull Call<CommonConfig> call, @NonNull Response<CommonConfig> response) {
            config = response.body();
        }

        @Override
        public void onFailure(@NonNull Call<CommonConfig> call, @NonNull Throwable t) {
            call.cancel();
        }
    }

    private static class MarkuStationConfigCallback implements Callback<MarkuStationConfig> {
        @Override
        public void onResponse(@NonNull Call<MarkuStationConfig> call, @NonNull Response<MarkuStationConfig> response) {
            markuStationConfig = response.body();
        }

        @Override
        public void onFailure(@NonNull Call<MarkuStationConfig> call, @NonNull Throwable t) {
            call.cancel();
        }
    }
}