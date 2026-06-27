package ee.mas.integratsioonitarkvara;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class Welcome extends Fragment {
    public Welcome() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_welcome, container, false);
        var vw = ((WebView)view.findViewById(R.id.webView));
        vw.loadUrl("https://markuseasjad.blogspot.com");
        vw.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                var first = view.getRootView().findViewById(R.id.progressBar);
                if (first == null) return;
                var rootActivity = ((Activity)first.getContext());
                if (rootActivity instanceof MainActivity) {
                    rootActivity.findViewById(R.id.progressBar).setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                var first = view.getRootView().findViewById(R.id.progressBar);
                if (first == null) return;
                var rootActivity = ((Activity)first.getContext());
                if (rootActivity instanceof MainActivity) {
                    rootActivity.findViewById(R.id.progressBar).setVisibility(GONE);
                    rootActivity.findViewById(R.id.error).setVisibility(GONE);
                }
                vw.getSettings().setJavaScriptEnabled(true);
                if (checkIsTablet() && vw.getUrl().equals("https://markuseasjad.blogspot.com/")) {
                    view.loadUrl("javascript:(function() { document.querySelector(\".blogger\").style.paddingRight = \"20vw\"; document.querySelector(\"body\").style.width = \"120vw\"; document.querySelector(\".copyright\").style.paddingRight = \"20vw\"; document.querySelector(\"body\").style.overflowX = \"hidden\";  })()");
                }
                view.loadUrl("javascript:(function() { document.querySelector(\".blog-name\").style.display = \"none\"; document.querySelector(\".search\").style.display = \"none\"; document.querySelector(\".top-nav\").style.display = \"none\"; document.querySelector(\".sidebar\").style.display = \"none\"; })()");
                vw.getSettings().setJavaScriptEnabled(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !(request.getUrl().toString().startsWith("https://markuseasjad.blogspot.com"));
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (error.getErrorCode() == ERROR_HOST_LOOKUP) {
                    view.setVisibility(GONE);
                    view.getRootView().findViewById(R.id.error).setVisibility(VISIBLE);
                }
            }
        });
        return view;
    }
    private boolean checkIsTablet() {
        boolean isTablet = false;
        var activity = ((Activity)   this.getContext());
        if (activity == null) return false;
        var wm = activity.getWindowManager();
        if (wm == null) return false;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        if (diagonalInches >= 7.0) {
            isTablet = true;
        }

        return isTablet;
    }
}