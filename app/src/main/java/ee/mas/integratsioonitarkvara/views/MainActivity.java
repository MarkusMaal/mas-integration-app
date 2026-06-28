package ee.mas.integratsioonitarkvara.views;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ComponentCaller;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

import ee.mas.integratsioonitarkvara.DialogBuilders;
import ee.mas.integratsioonitarkvara.R;
import ee.mas.integratsioonitarkvara.SharedPrefHelpers;
import ee.mas.integratsioonitarkvara.WolHelpers;
import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.DesktopCommand;
import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.Edition;
import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;
import ee.mas.integratsioonitarkvara.models.Scheme;
import ee.mas.integratsioonitarkvara.services.ApiService;

import ee.mas.integratsioonitarkvara.views.fragments.AboutFragment;
import ee.mas.integratsioonitarkvara.views.fragments.ConfigFragment;
import ee.mas.integratsioonitarkvara.views.fragments.DesktopFragment;
import ee.mas.integratsioonitarkvara.views.fragments.MarkuStationFragment;
import ee.mas.integratsioonitarkvara.views.fragments.Welcome;
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
    public static DesktopLayout desktopLayout;

    private static Scheme scheme;

    private static ApiService apiService;

    public enum Tabs {
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
            Request request = chain.request().newBuilder().addHeader("Auth", SharedPrefHelpers.getSettingsAuth(this)).build();
            return chain.proceed(request);
        });
        var endPoint = SharedPrefHelpers.getSettingsUrl(this);
        apiService = new Retrofit.Builder()
                .baseUrl(endPoint)
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
                    tab.setText(R.string.uudised);
                    break;
                case MARKUSTATION:
                    tab.setText(R.string.markustation);
                    break;
                case CONFIG:
                    tab.setText(R.string.konfiguratsioon);
                    break;
                case DESKTOP:
                    tab.setText(R.string.t_laud);
                    break;
                case ABOUT:
                    tab.setText(R.string.teave);
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
        var ab = getSupportActionBar();
        if (ab != null) ab.hide();
        try {
            SharedPrefHelpers.updateVersion(this.getBaseContext().getApplicationContext());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadBackground(endPoint);
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
                enqueue(apiService.getScheme(), s -> scheme = s, this);
                var imv = (ImageView) findViewById(R.id.desktopBackground);
                var iml = (ImageView) findViewById(R.id.loginBackground);
                var imu = (ImageView) findViewById(R.id.uncommonBackground);
                var imt = (ImageView) findViewById(R.id.deviceBackground);
                var imtl = (ImageView) findViewById(R.id.deviceLockScreen);
                if (imv == null) break;
                var endpoint = SharedPrefHelpers.getSettingsUrl(this);
                imv.setOnClickListener(v -> saveToGallery(endpoint, "bg_desktop.png"));
                iml.setOnClickListener(v -> saveToGallery(endpoint, "bg_login.png"));
                imu.setOnClickListener(v -> saveToGallery(endpoint, "bg_uncommon.png"));
                imt.setOnClickListener(v -> saveToGallery(endpoint, Welcome.checkIsTablet(this) ? "bg_tablet.png" : "bg_mobile.png"));
                imtl.setOnClickListener(v -> saveToGallery(endpoint, Welcome.checkIsTablet(this) ? "bg_tablet_lock.png" : "bg_mobile_lock.png"));
                Glide.with(this).load(endpoint + "/mas/bg_desktop.png").into(imv);
                Glide.with(this).load(endpoint + "/mas/bg_login.png").into(iml);
                Glide.with(this).load(endpoint + "/mas/bg_uncommon.png").into(imu);
                if (Welcome.checkIsTablet(this)) {
                    Glide.with(this).load(endpoint + "/mas/bg_tablet.png").into(imt);
                    Glide.with(this).load(endpoint + "/mas/bg_tablet_lock.png").into(imtl);
                } else {
                    Glide.with(this).load(endpoint + "/mas/bg_mobile.png").into(imt);
                    Glide.with(this).load(endpoint + "/mas/bg_mobile_lock.png").into(imtl);
                }
                break;
            case DESKTOP:
                enqueue(apiService.getDesktopLayout(), d -> desktopLayout = d, this);
                break;
        }
    }

    private void loadBackground(String endpoint) {
        var fullUrl = endpoint + "/mas/bg_common.png";
        var pg = (ImageView) findViewById(R.id.backgroundImage);
        Glide.with(this).load(fullUrl).into(pg);
    }

    public void saveToGallery(String endpoint, String url) {
        Toast t = new Toast(this);
        t.setText(getString(R.string.downloading_background) + " (" + url + ")");
        t.show();
        var thr = new Thread(() -> {
            try {
                var fullUrl = endpoint + "/mas/" + url;
                InputStream inputStream = new URL(fullUrl).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, url);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri uri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);

                if (uri != null) {
                    try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                        if (out == null) throw new IOException();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() -> {
                    var t2 = new Toast(this);
                    t2.setText(R.string.success);
                    t2.show();
                });
            } catch (IOException ignore){

            }
        });
        thr.start();
    }

    public static void sendCommand(String type, String args, Context ctx) {
        var desktopCommand = new DesktopCommand();
        desktopCommand.setArguments(args);
        desktopCommand.setType(type);
        enqueue(apiService.sendDesktopCommand(desktopCommand), null, ctx);
    }

    private static <T> void enqueue(Call<T> call, Consumer<T> onSuccess, Context context) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call,
                                   @NonNull Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (context instanceof MainActivity) {
                        ((MainActivity)context).findViewById(R.id.progressBar).setVisibility(GONE);
                    }
                    if (onSuccess == null) return;
                    onSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call,
                                  @NonNull Throwable t) {
                Log.println(Log.ERROR, "API request failed", Objects.requireNonNull(t.getMessage()));
                if (context instanceof MainActivity) {
                    ((MainActivity)context).findViewById(R.id.progressBar).setVisibility(GONE);
                }
                if (onSuccess == null) return;
                onSuccess.accept(null);
            }
        });
    }

    public void refresh(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void updateScheme(View v) {
        if (v instanceof Button) {
            var b = (Button)v;
            if (b.getId() == R.id.bgButton) {
                DialogBuilders.showColorPickerDialog(this, c -> scheme.setBackgroundColor(c), scheme);
                saveConfig(MainActivity.Tabs.CONFIG, v.getContext());
            } else if (b.getId() == R.id.fgButton) {
                DialogBuilders.showColorPickerDialog(this, c -> scheme.setForegroundColor(c), scheme);
                saveConfig(MainActivity.Tabs.CONFIG, v.getContext());
            }
        }
    }

    public void wakeUp(View v) {
        WolHelpers.wakeUp(v);
    }

    public void appSettings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public static void saveConfig(Tabs tab, Context ctx) {
        switch (tab) {
            case CONFIG:
                enqueue(apiService.saveCommonConfig(config), cc -> config = cc, ctx);
                enqueue(apiService.saveScheme(scheme), s -> scheme = s, ctx);
                break;
            case MARKUSTATION:
                enqueue(apiService.saveMarkuStationConfig(markuStationConfig), mc -> markuStationConfig = mc, ctx);
                enqueue(apiService.saveMarkuStationGames(markuStationGames), mg -> markuStationGames = mg, ctx);
                break;
            case DESKTOP:
                enqueue(apiService.saveDesktopLayout(desktopLayout), dl -> desktopLayout = dl, ctx);
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
        MainActivity.saveConfig(Tabs.CONFIG, this);
    }

    public void desktopCheckboxClick(View view) {
        try {
            if (view instanceof CheckBox) {
                var cb = (CheckBox) view;
                var desktopCommand = new DesktopCommand();
                desktopCommand.setArguments(Boolean.toString(cb.isChecked()));
                if (cb.getId() == R.id.desktopIconsCheckbox) {
                    desktopCommand.setType("IsIconVisible");
                    desktopLayout.setShowIcons(cb.isChecked());
                } else if (cb.getId() == R.id.markusStuffLogoCheckbox) {
                    desktopCommand.setType("IsLogoVisible");
                    desktopLayout.setShowLogo(cb.isChecked());
                } else if (cb.getId() == R.id.controlsCheckbox) {
                    desktopCommand.setType("IsActionVisible");
                    desktopLayout.setShowActions(cb.isChecked());
                } else if (cb.getId() == R.id.lockCheckbox) {
                    desktopCommand.setType("Lock");
                    desktopLayout.setLockIcons(cb.isChecked());
                }
                enqueue(apiService.sendDesktopCommand(desktopCommand), null, this);
            }
        } catch (NullPointerException e) {
            var msg = e.getMessage();
            if (msg != null) {
                Log.println(Log.ERROR, "Post request failed", msg);
            }
        }
    }


    public void markuStationCheckboxClick(View view) {
        try {
            if (view instanceof CheckBox) {
                var cb = (CheckBox) view;
                if (cb.getId() == R.id.introCheckbox)
                    markuStationConfig.setPlayIntro(cb.isChecked());
                else if (cb.getId() == R.id.creepypastaCheckbox)
                    markuStationConfig.setCreepypastaIntro(cb.isChecked());
                else if (cb.getId() == R.id.legacyCheckbox)
                    markuStationConfig.setLegacyIntro(cb.isChecked());
                else if (cb.getId() == R.id.specialCheckbox)
                    markuStationConfig.setSpecialIntro(cb.isChecked());
            }
            MainActivity.saveConfig(Tabs.MARKUSTATION, this);
        } catch (NullPointerException e) {
            var msg = e.getMessage();
            if (msg != null) {
                Log.println(Log.ERROR, "Post request failed", msg);
            }
        }
    }
}