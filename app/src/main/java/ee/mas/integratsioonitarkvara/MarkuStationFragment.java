package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static ee.mas.integratsioonitarkvara.DesktopFragment.getListViewSize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

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
        var gamesView = ((ListView)view.findViewById(R.id.gamesListView));
        SimpleAdapter adapter = getSimpleAdapter(view);
        gamesView.setAdapter(adapter);
        getListViewSize(gamesView);
    }

    @NonNull
    private static SimpleAdapter getSimpleAdapter(View view) {
        var gamesList = new ArrayList<HashMap<String, String>>();
        for (var g : MainActivity.markuStationGames) {
            HashMap<String,String> game = new HashMap<>();
            game.put("Title", g.getName());
            game.put("Executable", g.getExecutable());
            gamesList.add(game);
        }
        return new SimpleAdapter(view.getContext().getApplicationContext(), gamesList, android.R.layout.simple_list_item_2, new String[] {"Title", "Executable"}, new int[] {android.R.id.text1, android.R.id.text2});
    }
}