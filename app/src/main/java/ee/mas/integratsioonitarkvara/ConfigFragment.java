package ee.mas.integratsioonitarkvara;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import ee.mas.integratsioonitarkvara.models.CommonConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {
    private final CommonConfig config;

    public ConfigFragment() {
        this.config = null;
    }

    public ConfigFragment(CommonConfig config) {
        this.config = config;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_config, container, false);
        if (config == null) return view;
        // Inflate the layout for this fragment
        CheckBox logoCheck = view.findViewById(R.id.logoCheckbox);
        CheckBox scheduleCheck = view.findViewById(R.id.scheduleCheckbox);
        CheckBox notesCheck = view.findViewById(R.id.desktopNotesCheckbox);
        EditText pollRate = view.findViewById(R.id.pollRateTextbox);
        logoCheck.setChecked(config.isShowLogo());
        scheduleCheck.setChecked(config.isAllowScheduledTasks());
        notesCheck.setChecked(config.isAutostartNotes());
        pollRate.setText(String.valueOf(config.getPollRate()));
        return view;
    }
}