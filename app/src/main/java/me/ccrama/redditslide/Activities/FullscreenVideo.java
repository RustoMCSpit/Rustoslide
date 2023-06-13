package me.ccrama.redditslide.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;

import ltd.ucode.slide.Preferences;
import ltd.ucode.slide.R;
import ltd.ucode.slide.App;
import me.ccrama.redditslide.util.LinkUtil;
import me.ccrama.redditslide.util.LogUtil;


/**
 * Created by ccrama on 3/5/2015.
 */
public class FullscreenVideo extends FullScreenActivity {

    public static final String EXTRA_HTML = "html";
    private WebView v;

    @Override
    public void finish() {
        super.finish();
        v.loadUrl("about:blank");
        overridePendingTransition(0, R.anim.fade_out);
    }

    public void onCreate(Bundle savedInstanceState) {
        overrideRedditSwipeAnywhere();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        String data = getIntent().getExtras().getString(EXTRA_HTML);
        v = (WebView) findViewById(R.id.webgif);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        String dat = data;
        final WebSettings settings = v.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);

        v.setWebChromeClient(new WebChromeClient());
        LogUtil.v(dat);

        if (dat.contains("src=\"")) {
            int start = dat.indexOf("src=\"") + 5;
            dat = dat.substring(start, dat.indexOf("\"", start));
            if(dat.startsWith("//")){
                dat = "https:" + dat;
            }
            LogUtil.v(dat);
            setShareUrl(dat);
            v.loadUrl(dat);
            if ((dat.contains("youtube.co" ) || dat.contains("youtu.be")) && !Preferences.INSTANCE.getAppRestart().contains("showYouTubePopup")) {
                new AlertDialog.Builder(FullscreenVideo.this)
                        .setTitle(R.string.load_videos_internally)
                        .setMessage(R.string.load_videos_internally_content)
                        .setPositiveButton(R.string.btn_sure, (dialog, which) ->
                                LinkUtil.launchMarketUri(
                                        FullscreenVideo.this, R.string.youtube_plugin_package))
                        .setNegativeButton(R.string.btn_no, null)
                        .setNeutralButton(R.string.do_not_show_again, (dialog, which) ->
                                Preferences.INSTANCE.getAppRestart().edit().putBoolean("showYouTubePopup", false).apply())
                        .show();
            }
        } else {
            LogUtil.v(dat);
            v.loadDataWithBaseURL("", dat, "text/html", "utf-8", "");
        }
    }
}
