package ee.mas.integratsioonitarkvara;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.preference.PreferenceManager;

public class SharedPrefHelpers {
    public static String getSettingsUrl(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        var ip = sp.getString("ip", ctx.getString(R.string.defaultIp));
        var port = sp.getString("port", ctx.getString(R.string.defaultPort));
        return "http://" + ip + ":" + port;
    }

    public static String getSettingsAuth(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sp.getString("code", "");
    }

    public static void updateVersion(Context ctx) throws PackageManager.NameNotFoundException {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("app-version", ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName);
        editor.apply();
    }

}
