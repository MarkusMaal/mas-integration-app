package ee.mas.integratsioonitarkvara;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;

public class DialogBuilders {
    public static void ShowMsEditDialog(Context ctx, int position, MarkuStationGame[] games) {
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
            games[position].setName(name.getText().toString());
            games[position].setExecutable(location.getText().toString());
            dialog.dismiss();
        });
        builder.setCancelable(false);
        AlertDialog ad = builder.create();
        ad.setTitle(ctx.getString(R.string.edit_game));
        ad.show();
    }

    public static void ShowDesktopEditDialog(Context ctx, int position, DesktopLayout layout) {
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

    public static void ShowDesktopCreateDialog(Context ctx, DesktopLayout layout) {
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

    private static void removeElement(Object[] arr, int removedIdx) {
        System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
    }
}
