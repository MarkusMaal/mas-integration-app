package ee.mas.integratsioonitarkvara;

import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
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

    private enum Tabs {
        WELCOME,
        MARKUSTATION,
        CONFIG,
        DESKTOP,
        ABOUT
    }


    @Override
    public void onNewIntent(@NonNull Intent intent, @NonNull ComponentCaller caller) {
        Refresh();
        super.onNewIntent(intent, caller);
    }

    @Override
    protected void onRestart() {
        Refresh();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Refresh();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(ContextCompat.getColor(this, R.color.purple_700)));
        setContentView(R.layout.activity_main);

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


    public void Refresh() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.201:14415")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getConfig().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonConfig> call, @NonNull Response<CommonConfig> response) {
                if (!response.isSuccessful()) return;
                config = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<CommonConfig> call, @NonNull Throwable t) {
            }
        });

        apiService.getEdition().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Edition> call, @NonNull Response<Edition> response) {
                if (!response.isSuccessful()) return;
                edition = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<Edition> call, @NonNull Throwable t) {}
        });

        apiService.getMarkuStationConfig().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MarkuStationConfig> call, @NonNull Response<MarkuStationConfig> response) {
                if (!response.isSuccessful()) return;
                markuStationConfig = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<MarkuStationConfig> call, @NonNull Throwable t) {

            }
        });

        apiService.getMarkuStationGames().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MarkuStationGame[]> call, @NonNull Response<MarkuStationGame[]> response) {
                if (!response.isSuccessful()) return;
                markuStationGames = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<MarkuStationGame[]> call, @NonNull Throwable t) {

            }
        });

        apiService.getDesktopLayout().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<DesktopLayout> call, @NonNull Response<DesktopLayout> response) {
                if (!response.isSuccessful()) return;
                desktopLayout = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<DesktopLayout> call, @NonNull Throwable t) {

            }
        });
    }

    public void Refresh(View view) {
        Refresh();
    }
}