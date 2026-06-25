package ee.mas.integratsioonitarkvara;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ee.mas.integratsioonitarkvara.models.Edition;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private final Edition edition;
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
        if (edition == null) return view;
        TextView tv = view.findViewById(R.id.textView);
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
        return view;
    }
}