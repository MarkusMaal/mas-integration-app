package ee.mas.integratsioonitarkvara;

import android.content.SharedPreferences;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WolHelpers {
    // copy-pasted from maia-app with minor modifications
    public static void wakeUp(View v) {
        Thread thread = new Thread(() -> {
            try {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                var ip = sp.getString("ip", v.getContext().getString(R.string.defaultIp));
                var wp = sp.getString("wol-port", "9");
                var mac = sp.getString("wol-mac", "00:00:00:00:00:00").toUpperCase();
                int port = Integer.parseInt(wp);
                byte[] macBytes = getMacBytes(mac);
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xff;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }

                InetAddress address = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();

                Snackbar snb = Snackbar.make(v, v.getContext().getString(R.string.packetSent), 2000);
                snb.show();
            } catch (Exception e) {
                Snackbar snb = Snackbar.make(v, v.getContext().getString(R.string.packetFail) + " " + e.getMessage(), 2000);
                snb.show();
            }

        });
        thread.start();
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("([:\\-])");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

}
