package ee.mas.integratsioonitarkvara;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkuStationFragment extends Fragment {
    private final MarkuStationConfig config;
    private final MarkuStationGame[] games;

    public MarkuStationFragment() {
        this.config = null;
        this.games = null;
    }
    public MarkuStationFragment(MarkuStationConfig config, MarkuStationGame[] games) {
        this.config = config;
        this.games = games;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_markustation, container, false);
        if (config == null) return view;
        ((CheckBox)view.findViewById(R.id.introCheckbox)).setChecked(config.isPlayIntro());
        ((CheckBox)view.findViewById(R.id.creepypastaCheckbox)).setChecked(config.isCreepypastaIntro());
        ((CheckBox)view.findViewById(R.id.legacyCheckbox)).setChecked(config.isLegacyIntro());
        ((CheckBox)view.findViewById(R.id.specialCheckbox)).setChecked(config.isSpecialIntro());
        ((Spinner)view.findViewById(R.id.monitorModeSpinner)).setSelection(config.getMonitorMode());
        return view;
    }
}