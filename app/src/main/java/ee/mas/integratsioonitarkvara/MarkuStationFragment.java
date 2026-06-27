package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkuStationFragment extends Fragment {

    public MarkuStationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_markustation, container, false);
        updateMarkuStation(MainActivity.markuStationConfig, MainActivity.markuStationGames, view);
        ((Spinner)view.findViewById(R.id.monitorModeSpinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.markuStationConfig == null) return;
                MainActivity.markuStationConfig.setMonitorMode(position);
                MainActivity.saveConfig(MainActivity.Tabs.MARKUSTATION);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        return view;
    }

    public void updateMarkuStation(MarkuStationConfig config, MarkuStationGame[] games, View view) {
        if (config == null) {
            view.findViewById(R.id.markuStationContainer).setVisibility(GONE);
            view.findViewById(R.id.error).setVisibility(VISIBLE);
            return;
        }
        view.findViewById(R.id.markuStationContainer).setVisibility(VISIBLE);
        view.findViewById(R.id.error).setVisibility(GONE);
        ((CheckBox)view.findViewById(R.id.introCheckbox)).setChecked(config.isPlayIntro());
        ((CheckBox)view.findViewById(R.id.creepypastaCheckbox)).setChecked(config.isCreepypastaIntro());
        ((CheckBox)view.findViewById(R.id.legacyCheckbox)).setChecked(config.isLegacyIntro());
        ((CheckBox)view.findViewById(R.id.specialCheckbox)).setChecked(config.isSpecialIntro());
        ((Spinner)view.findViewById(R.id.monitorModeSpinner)).setSelection(config.getMonitorMode());
    }
}