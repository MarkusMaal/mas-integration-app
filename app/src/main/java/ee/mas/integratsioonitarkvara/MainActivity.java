package ee.mas.integratsioonitarkvara;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.Edition;
import ee.mas.integratsioonitarkvara.services.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(ContextCompat.getColor(this, R.color.purple_700)));
        setContentView(R.layout.activity_main);

        Refresh();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Refresh() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.201:14415")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getConfig().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CommonConfig> call, Response<CommonConfig> response) {
                if (!response.isSuccessful()) return;
                CommonConfig config = response.body();
                CheckBox logoCheck = findViewById(R.id.logoCheckbox);
                CheckBox scheduleCheck = findViewById(R.id.scheduleCheckbox);
                CheckBox notesCheck = findViewById(R.id.desktopNotesCheckbox);
                EditText pollRate = findViewById(R.id.pollRateTextbox);
                logoCheck.setChecked(config.isShowLogo());
                scheduleCheck.setChecked(config.isAllowScheduledTasks());
                notesCheck.setChecked(config.isAutostartNotes());
                pollRate.setText(String.valueOf(config.getPollRate()));
            }

            @Override
            public void onFailure(Call<CommonConfig> call, Throwable t) {
                TextView tv = findViewById(R.id.textView);
                tv.setText("Error: " + t.getMessage());
            }
        });

        apiService.getEdition().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Edition> call, Response<Edition> response) {
                if (!response.isSuccessful()) return;
                Edition edition = response.body();
                TextView tv = findViewById(R.id.textView);
                StringBuilder sb = new StringBuilder();
                sb.append("Väljaanne: ").append(edition.getEditionName()).append("\n");
                sb.append("Versioon: ").append(edition.getVersion()).append("\n");
                sb.append("Nimi: ").append(edition.getName()).append("\n");
                sb.append("Järk: ").append(edition.getBuildNo()).append("\n");
                sb.append("Testitud: ").append(edition.isTested() ? "Jah" : "Ei").append("\n");
                sb.append("Kasutajanimi: ").append(edition.getUsername()).append("\n");
                sb.append("Keel: ").append(edition.getLanguage()).append("\n");
                sb.append("Tuuma versioon: ").append(edition.getWinVer()).append("\n");
                sb.append("Funktsioonid: ").append('\n');
                for (var f : edition.getFeatures()) {
                    switch (f) {
                        case "IP":
                            sb.append(" - Integratsioonitarkvara\n");
                            break;
                        case "WX":
                            sb.append(" - Windows 10+\n");
                            break;
                        case "RM":
                            sb.append(" - Rainmeter\n");
                            break;
                        case "GP":
                            sb.append(" - Grupipoliitika\n");
                            break;
                        case "LT":
                            sb.append(" - LiveTuner optimeerimised\n");
                            break;
                        case "DX":
                            sb.append(" - DesktopX\n");
                            break;
                        case "CS":
                            sb.append(" - Klassikaline start menüü\n");
                            break;
                        case "RD":
                            sb.append(" - Kaugjuhtimine\n");
                            break;
                        case "TS":
                            sb.append(" - Interaktiivne töölaud\n");
                            break;
                        case "MM":
                            sb.append(" - Standardfunktsioonid\n");
                            break;
                        default:
                            sb.append(" - ???\n");
                            break;
                    }
                }
                sb.append("Ebaturvaline PIN kood: ").append(edition.getPin()).append("\n");
                sb.append("Verifile räsi: ").append(edition.getHash().substring(0, 10)).append("\n");
                tv.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<Edition> call, Throwable t) {
                TextView tv = findViewById(R.id.textView);
                tv.setText("Error: " + t.getMessage());
            }
        });
    }

    public void Refresh(View view) {
        Refresh();
    }
}