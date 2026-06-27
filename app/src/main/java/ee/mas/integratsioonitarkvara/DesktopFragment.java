package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ee.mas.integratsioonitarkvara.models.DesktopLayout;

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
        ((EditText)view.findViewById(R.id.iconsXBox)).setText(String.valueOf(desktopLayout.getIconCountX()));
        ((EditText)view.findViewById(R.id.iconsYBox)).setText(String.valueOf(desktopLayout.getIconCountY()));
        ((EditText)view.findViewById(R.id.marginPxBox)).setText(String.valueOf(desktopLayout.getIconPadding()));
        ((EditText)view.findViewById(R.id.relativeSizeBox)).setText(String.valueOf(desktopLayout.getIconSize()));
        ((CheckBox)view.findViewById(R.id.lockCheckbox)).setChecked(desktopLayout.isLockIcons());
        ((CheckBox)view.findViewById(R.id.desktopIconsCheckbox)).setChecked(desktopLayout.isShowIcons());
        ((CheckBox)view.findViewById(R.id.markusStuffLogoCheckbox)).setChecked(desktopLayout.isShowLogo());
        ((CheckBox)view.findViewById(R.id.controlsCheckbox)).setChecked(desktopLayout.isShowActions());
        var appsView = ((ListView)view.findViewById(R.id.appsListView));
        var icons = new ArrayList<String>();
        for (var i : desktopLayout.getChildren()) {
            icons.add(i.getIcon() + ": " + i.getExecutable());
        }
        ArrayAdapter<String> arr = new ArrayAdapter<>(view.getContext().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, icons);
        appsView.setAdapter(arr);
        getListViewSize(appsView);
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
            lastHeight = listItem.getMeasuredHeight() * 2;
            totalHeight += lastHeight;
        }
        totalHeight += lastHeight * 2;
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=(totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1)));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }
}