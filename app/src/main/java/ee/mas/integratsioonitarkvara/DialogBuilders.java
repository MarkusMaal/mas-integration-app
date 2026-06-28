package ee.mas.integratsioonitarkvara;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mrudultora.colorpicker.ColorPickerPopUp;

import java.util.function.Consumer;

import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;
import ee.mas.integratsioonitarkvara.models.Scheme;
import ee.mas.integratsioonitarkvara.views.MainActivity;

public class DialogBuilders {
    public static void showMsEditDialog(Context ctx, int position, MarkuStationGame[] games, Consumer<MarkuStationGame> onOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View msE = LayoutInflater.from(ctx).inflate(R.layout.fragment_markustation_edit, null);
        final EditText name = msE.findViewById(R.id.gameNameBox);
        final EditText location = msE.findViewById(R.id.gameLocationBox);
        name.setText(games[position].getName());
        location.setText(games[position].getExecutable());
        var p = 40;
        msE.setPadding(p, p, p, p);
        builder.setView(msE)
        .setNeutralButton(R.string.delete, (dialog, which) -> {
            removeElement(games, position);
        })
        .setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.cancel();
        })
        .setPositiveButton(R.string.ok, (dialog, which) -> {
            MainActivity.markuStationGames[position].setName(name.getText().toString());
            MainActivity.markuStationGames[position].setExecutable(location.getText().toString());
            MainActivity.saveConfig(MainActivity.Tabs.MARKUSTATION, ctx);
            onOk.accept(MainActivity.markuStationGames[position]);
            dialog.dismiss();
        });
        builder.setCancelable(false);
        AlertDialog ad = builder.create();
        ad.setTitle(ctx.getString(R.string.edit_game));
        ad.show();
    }

    public static void showDesktopEditDialog(Context ctx, int position, DesktopLayout layout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View dE = LayoutInflater.from(ctx).inflate(R.layout.fragment_desktop_edit, null);
        final Spinner icon = dE.findViewById(R.id.iconChooser);
        final EditText executable = dE.findViewById(R.id.commandLineBox);
        var selectedIcon = layout.getChildren()[position];
        for (var i = 0; i < icon.getCount(); i++) {
            var str = icon.getItemAtPosition(i).toString();
            if (str.equals(selectedIcon.getIcon())) {
                icon.setSelection(i);
                break;
            }
        }
        executable.setText(selectedIcon.getExecutable());
        var p = 40;
        dE.setPadding(p, p, p, p);
        builder.setView(dE)
                .setNeutralButton(R.string.delete, (dialog, which) -> {
                    removeElement(layout.getChildren(), position);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    selectedIcon.setIcon(icon.getSelectedItem().toString());
                    selectedIcon.setExecutable(executable.getText().toString());
                    dialog.dismiss();
                });
        builder.setCancelable(false);
        AlertDialog ad = builder.create();
        ad.setTitle(ctx.getString(R.string.edit_desktop_icon));
        ad.show();
    }

    public static void showDesktopCreateDialog(Context ctx, DesktopLayout layout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View dE = LayoutInflater.from(ctx).inflate(R.layout.fragment_desktop_edit, null);
        final Spinner icon = dE.findViewById(R.id.iconChooser);
        final EditText executable = dE.findViewById(R.id.commandLineBox);
        var p = 40;
        dE.setPadding(p, p, p, p);
        builder.setView(dE)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.setCancelable(false);
        AlertDialog ad = builder.create();
        ad.setTitle(ctx.getString(R.string.add_desktop_icon));
        ad.show();
    }

    public static void showColorPickerDialog(Context context, Consumer<ee.mas.integratsioonitarkvara.models.Color> onOk, Scheme scheme) {
        var c = Color.valueOf(scheme.getBackgroundColor().getR() / 255f, scheme.getBackgroundColor().getG() / 255f, scheme.getBackgroundColor().getB() / 255f, scheme.getBackgroundColor().getA() / 255f);
        ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(context);
        colorPickerPopUp.setShowAlpha(false)
                .setDefaultColor(c.toArgb())
                .setDialogTitle(context.getString(R.string.vali_taustav_rv))
                .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                    @Override
                    public void onColorPicked(int color) {
                        onOk.accept(processColor(color));
                    }

                    @Override
                    public void onCancel() {
                        colorPickerPopUp.dismissDialog();	// Dismiss the dialog.
                    }
                })
                .show();
    }


    private static ee.mas.integratsioonitarkvara.models.Color processColor(int argb) {
        var c = Color.valueOf(argb);
        var mc = new ee.mas.integratsioonitarkvara.models.Color();
        mc.setA((int)(255f * c.alpha()));
        mc.setR((int)(255f * c.red()));
        mc.setG((int)(255f * c.green()));
        mc.setB((int)(255f * c.blue()));
        return mc;
    }
    private static void removeElement(Object[] arr, int removedIdx) {
        System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
    }
}
