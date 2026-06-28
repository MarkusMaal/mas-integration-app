package ee.mas.integratsioonitarkvara.views.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import ee.mas.integratsioonitarkvara.R;
import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.views.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    public ConfigFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_config, container, false);
        updateConfig(MainActivity.config, view);
        ((EditText)view.findViewById(R.id.pollRateTextbox)).addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 1) return;
                try
                {
                    var val = Integer.parseInt(s.toString());
                    if (MainActivity.config == null) return;
                    MainActivity.config.setPollRate(val);
                } catch (NumberFormatException ignored) {

                }
                MainActivity.saveConfig(MainActivity.Tabs.CONFIG, view.getContext());
            }
        });
        return view;
    }

    public void updateConfig(CommonConfig config, View view) {
        if (config == null) {
            view.findViewById(R.id.error).setVisibility(VISIBLE);
            view.findViewById(R.id.configLayout).setVisibility(GONE);
            return;
        }
        view.findViewById(R.id.error).setVisibility(GONE);
        view.findViewById(R.id.configLayout).setVisibility(VISIBLE);
        // Inflate the layout for this fragment
        CheckBox logoCheck = view.findViewById(R.id.logoCheckbox);
        CheckBox scheduleCheck = view.findViewById(R.id.scheduleCheckbox);
        CheckBox notesCheck = view.findViewById(R.id.desktopNotesCheckbox);
        EditText pollRate = view.findViewById(R.id.pollRateTextbox);
        logoCheck.setChecked(config.isShowLogo());
        scheduleCheck.setChecked(config.isAllowScheduledTasks());
        notesCheck.setChecked(config.isAutostartNotes());
        pollRate.setText(String.valueOf(config.getPollRate()));
    }
}