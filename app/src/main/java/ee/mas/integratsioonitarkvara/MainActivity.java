package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ComponentCaller;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static CommonConfig config;
    private static Edition edition;
    private static MarkuStationConfig markuStationConfig;

    private static MarkuStationGame[] markuStationGames;
    private static DesktopLayout desktopLayout;

    private boolean firstLoad = true;

    private final ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.201:14415")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    private enum Tabs {
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
        setContentView(R.layout.activity_main);

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
                    return new MarkuStationFragment(markuStationConfig, markuStationGames);
                case CONFIG:
                    return new ConfigFragment(config);
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
        refresh();
    }
}