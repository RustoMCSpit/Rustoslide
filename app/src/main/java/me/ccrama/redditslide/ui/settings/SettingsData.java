package me.ccrama.redditslide.ui.settings;

import android.os.Bundle;
import android.view.ViewGroup;

import ltd.ucode.slide.ui.BaseActivityAnim;
import ltd.ucode.slide.R;


/**
 * Created by ccrama on 3/5/2015.
 */
public class SettingsData extends BaseActivityAnim {

    private SettingsDataFragment fragment = new SettingsDataFragment(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyColorTheme();
        setContentView(R.layout.activity_settings_datasaving);
        setupAppBar(R.id.toolbar, R.string.settings_data, true, true);

        ((ViewGroup) findViewById(R.id.settings_datasaving)).addView(
                getLayoutInflater().inflate(R.layout.activity_settings_datasaving_child, null));

        fragment.Bind();
    }

}
