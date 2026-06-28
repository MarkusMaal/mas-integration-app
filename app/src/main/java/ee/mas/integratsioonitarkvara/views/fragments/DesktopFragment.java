package ee.mas.integratsioonitarkvara.views.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import ee.mas.integratsioonitarkvara.DialogBuilders;
import ee.mas.integratsioonitarkvara.R;
import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.views.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DesktopFragment extends Fragment {

    private DesktopLayout desktopLayout;

    public DesktopFragment() {
        // Required empty public constructor
    }

    public DesktopFragment(DesktopLayout desktopLayout) {
        this.desktopLayout = desktopLayout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_desktop, container, false);
        updateDesktop(desktopLayout, view);
        return view;
    }

    public void updateDesktop(DesktopLayout desktopLayout, View view) {
        this.desktopLayout = desktopLayout;
        if (desktopLayout == null) {
            view.findViewById(R.id.desktopLayoutContainer).setVisibility(GONE);
            view.findViewById(R.id.error).setVisibility(VISIBLE);
            return;
        }
        view.findViewById(R.id.desktopLayoutContainer).setVisibility(VISIBLE);
        view.findViewById(R.id.error).setVisibility(GONE);
        final EditText iconsXBox = view.findViewById(R.id.iconsXBox);
        final EditText iconsYBox = view.findViewById(R.id.iconsYBox);
        final EditText marginPxBox = view.findViewById(R.id.marginPxBox);
        final EditText relativeSizeBox = view.findViewById(R.id.relativeSizeBox);
        final Button applyButton = view.findViewById(R.id.applyButton);
        iconsXBox.setText(String.valueOf(desktopLayout.getIconCountX()));
        iconsYBox.setText(String.valueOf(desktopLayout.getIconCountY()));
        marginPxBox.setText(String.valueOf(desktopLayout.getIconPadding()));
        relativeSizeBox.setText(String.valueOf(desktopLayout.getIconSize()));
        ((CheckBox)view.findViewById(R.id.lockCheckbox)).setChecked(desktopLayout.isLockIcons());
        ((CheckBox)view.findViewById(R.id.desktopIconsCheckbox)).setChecked(desktopLayout.isShowIcons());
        ((CheckBox)view.findViewById(R.id.markusStuffLogoCheckbox)).setChecked(desktopLayout.isShowLogo());
        ((CheckBox)view.findViewById(R.id.controlsCheckbox)).setChecked(desktopLayout.isShowActions());
        var appsView = ((ListView)view.findViewById(R.id.appsListView));
        appsView.setOnItemClickListener((parent, view1, position, id) -> {
            DialogBuilders.showDesktopEditDialog(view.getContext(), position, desktopLayout);
        });
        final Button addAppButton = view.findViewById(R.id.addAppButton);
        addAppButton.setOnClickListener(view2 -> DialogBuilders.showDesktopCreateDialog(view.getContext(), desktopLayout));
        SimpleAdapter sa = getSimpleAdapter(desktopLayout, view);
        appsView.setAdapter(sa);
        getListViewSize(appsView);

        applyButton.setOnClickListener(v -> {
            try {
                MainActivity.desktopLayout.setIconCountX(Integer.parseInt(iconsXBox.getText().toString()));
                MainActivity.desktopLayout.setIconCountY(Integer.parseInt(iconsYBox.getText().toString()));
                MainActivity.desktopLayout.setIconSize(Integer.parseInt(relativeSizeBox.getText().toString()));
                MainActivity.desktopLayout.setIconPadding(Integer.parseInt(marginPxBox.getText().toString()));
            } catch (NumberFormatException ignored) {
                return;
            }
            MainActivity.saveConfig(MainActivity.Tabs.DESKTOP, v.getContext());
            MainActivity.sendCommand("Restart", "", v.getContext());
        });
    }

    @NonNull
    private static SimpleAdapter getSimpleAdapter(DesktopLayout desktopLayout, View view) {
        var icons = new ArrayList<HashMap<String, String>>();
        for (var i : desktopLayout.getChildren()) {
            HashMap<String, String> icon = new HashMap<>();
            icon.put("Icon", i.getIcon());
            icon.put("Executable", i.getExecutable());
            icons.add(icon);
        }
        SimpleAdapter sa = new SimpleAdapter(view.getContext().getApplicationContext(), icons, android.R.layout.simple_expandable_list_item_2, new String[] {"Icon", "Executable"}, new int[] {android.R.id.text1, android.R.id.text2});
        return sa;
    }


    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter=myListView.getAdapter();
        if (myListAdapter==null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight=0;
        var lastHeight = 0;
        for (int size=0; size < myListAdapter.getCount(); size++) {
            View listItem=myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            lastHeight = listItem.getMeasuredHeight();
            totalHeight += lastHeight;
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=(totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1)));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }
}