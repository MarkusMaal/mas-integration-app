package ee.mas.integratsioonitarkvara.views.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
        final FrameLayout editionGlass = view.findViewById(R.id.editionGlass);
        final TextView deviceLabel = view.findViewById(R.id.deviceLabel);
        final TextView editionLabel = view.findViewById(R.id.editionLabel);
        var fText = getString(R.string.markuse_arvuti_asjad) + " " + edition.getVersion() + " - " + edition.getName();
        deviceLabel.setText(fText);
        editionLabel.setText(edition.getEditionName());
        TextView tv = view.findViewById(R.id.textView);
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.j_rk)).append(edition.getBuildNo()).append("\n");
        sb.append(getString(R.string.testitud)).append(edition.isTested() ? getString(R.string.yes) : getString(R.string.no)).append("\n");
        sb.append(getString(R.string.kasutajanimi)).append(edition.getUsername()).append("\n");
        sb.append(getString(R.string.keel)).append(edition.getLanguage()).append("\n");
        sb.append(getString(R.string.tuuma_versioon)).append(edition.getWinVer()).append("\n");
        for (var f : edition.getFeatures()) {
            switch (f) {
                case "IP":
                    ((ImageView)view.findViewById(R.id.integrationStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "WX":
                    ((ImageView)view.findViewById(R.id.winXPlusStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "RM":
                case "DX":
                    ((ImageView)view.findViewById(R.id.rainmeterStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "GP":
                    ((ImageView)view.findViewById(R.id.groupPolicyStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "LT":
                    ((ImageView)view.findViewById(R.id.liveTunerStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "CS":
                    ((ImageView)view.findViewById(R.id.classicStartStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "RD":
                    ((ImageView)view.findViewById(R.id.remoteManagementStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "IT":
                    ((ImageView)view.findViewById(R.id.interactiveDesktopStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "TS":
                    ((ImageView)view.findViewById(R.id.standardFeatures)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                case "MM":
                    ((ImageView)view.findViewById(R.id.markusStuffSupportStatus)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tick, null));
                    break;
                default:
                    sb.append(" - ??? \n");
                    break;
            }
        }
        switch (edition.getEditionName().toLowerCase()) {
            case "starter":
                editionGlass.setBackgroundColor(Color.argb(1f, 0f, 0.8f, 0));
                break;
            case "basic":
            case "basic+":
                editionGlass.setBackgroundColor(Color.argb(1f, 1f, 0.8f, 0));
                break;
            case "premium":
                editionGlass.setBackgroundColor(Color.argb(1f, 0.5f, 0, 0));
                break;
            case "pro":
                editionGlass.setBackgroundColor(Color.argb(1f, 0.2f, 0.6745098f, 0.8666667f));
                break;
            case "ultimate":
                editionGlass.setBackgroundColor(Color.argb(1f, 0.57254905f, 0.023529412f, 0.7882353f));
                break;
        }
        sb.append(getString(R.string.ebaturvaline_pin_kood)).append(edition.getPin()).append("\n");
        sb.append(getString(R.string.verifile_r_si)).append(edition.getHash().substring(0, 10)).append("\n");
        tv.setText(sb.toString());
    }
}