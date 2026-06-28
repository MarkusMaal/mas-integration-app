package ee.mas.integratsioonitarkvara.views.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ee.mas.integratsioonitarkvara.R;
import ee.mas.integratsioonitarkvara.models.Edition;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private Edition edition;
    public AboutFragment() {
        this.edition = null;
    }
    public AboutFragment(Edition edition) {
        this.edition = edition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_about, container, false);
        updateEdition(edition, view);
        return view;
    }

    public void updateEdition(Edition newEdition, View view) {
        this.edition = newEdition;
        if (edition == null) {
            view.findViewById(R.id.textView).setVisibility(GONE);
            view.findViewById(R.id.error).setVisibility(VISIBLE);
            return;
        }
        view.findViewById(R.id.textView).setVisibility(VISIBLE);
        view.findViewById(R.id.error).setVisibility(GONE);
        TextView tv = view.findViewById(R.id.textView);
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.v_ljaanne)).append(edition.getEditionName()).append("\n");
        sb.append(getString(R.string.versioon)).append(edition.getVersion()).append("\n");
        sb.append(getString(R.string.nimi)).append(edition.getName()).append("\n");
        sb.append(getString(R.string.j_rk)).append(edition.getBuildNo()).append("\n");
        sb.append(getString(R.string.testitud)).append(edition.isTested() ? getString(R.string.yes) : getString(R.string.no)).append("\n");
        sb.append(getString(R.string.kasutajanimi)).append(edition.getUsername()).append("\n");
        sb.append(getString(R.string.keel)).append(edition.getLanguage()).append("\n");
        sb.append(getString(R.string.tuuma_versioon)).append(edition.getWinVer()).append("\n");
        sb.append(getString(R.string.funktsioonid)).append('\n');
        for (var f : edition.getFeatures()) {
            switch (f) {
                case "IP":
                    sb.append(" - " + getString(R.string.integratsioonitarkvara) + "\n");
                    break;
                case "WX":
                    sb.append(" - " + getString(R.string.windows_10) + "\n");
                    break;
                case "RM":
                    sb.append(" - " + getString(R.string.rainmeter) + "\n");
                    break;
                case "GP":
                    sb.append(" - " + getString(R.string.grupipoliitika) + "\n");
                    break;
                case "LT":
                    sb.append(" - " + getString(R.string.livetuner_optimeerimised) + "\n");
                    break;
                case "DX":
                    sb.append(" - " + getString(R.string.desktopx) + "\n");
                    break;
                case "CS":
                    sb.append(" - " + getString(R.string.klassikaline_start_men) + "\n");
                    break;
                case "RD":
                    sb.append(" - " + getString(R.string.kaugjuhtimine) + "\n");
                    break;
                case "IT":
                    sb.append(" - " + getString(R.string.interaktiivne_t_laud) + "\n");
                    break;
                case "TS":
                    sb.append(" - " + getString(R.string.standardfunktsioonid) + "\n");
                    break;
                case "MM":
                    sb.append(" - " + getString(R.string.markuse_asjade_tugi) + "\n");
                    break;
                default:
                    sb.append(" - ??? \n");
                    break;
            }
        }
        sb.append(getString(R.string.ebaturvaline_pin_kood)).append(edition.getPin()).append("\n");
        sb.append(getString(R.string.verifile_r_si)).append(edition.getHash().substring(0, 10)).append("\n");
        tv.setText(sb.toString());
    }
}