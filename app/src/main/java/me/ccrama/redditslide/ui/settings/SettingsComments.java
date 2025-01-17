package me.ccrama.redditslide.ui.settings;

import android.os.Bundle;
import android.view.ViewGroup;

import ltd.ucode.slide.ui.BaseActivityAnim;
import ltd.ucode.slide.R;

public class SettingsComments extends BaseActivityAnim {

    private SettingsCommentsFragment fragment = new SettingsCommentsFragment(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyColorTheme();
        setContentView(R.layout.activity_settings_comments);
        setupAppBar(R.id.toolbar, R.string.settings_title_comments, true, true);

        ((ViewGroup) findViewById(R.id.settings_comments)).addView(
                getLayoutInflater().inflate(R.layout.activity_settings_comments_child, null));

        fragment.Bind();
    }

}
