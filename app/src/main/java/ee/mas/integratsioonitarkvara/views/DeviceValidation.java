package ee.mas.integratsioonitarkvara.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ee.mas.integratsioonitarkvara.R;

public class DeviceValidation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(ContextCompat.getColor(this, R.color.purple_700)));
        setContentView(R.layout.activity_device_validation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onAddButtonClick(View v) {
        MainActivity.enqueueVerifyCode(((EditText)this.findViewById(R.id.verificationTextBox)).getText().toString(), v.getContext());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}