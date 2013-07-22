package com.project.latviankeyboard;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

public class PrefsExtraRow extends PreferenceActivity {

	OnPreferenceClickListener myListener;
	SeekBar redBar, greenBar, blueBar, alphaBar;
	View colorTest;
	LinearLayout colorChooser;
	SeekBar.OnSeekBarChangeListener seekBarChangeListener;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_extra_row);
		// inflates layout
		colorChooser = (LinearLayout) getLayoutInflater().inflate(
				R.layout.color_chooser, null);
		// gets seekbars
		redBar = (SeekBar) colorChooser.findViewById(R.id.sbRed);
		greenBar = (SeekBar) colorChooser.findViewById(R.id.sbGreen);
		blueBar = (SeekBar) colorChooser.findViewById(R.id.sbBlue);
		alphaBar = (SeekBar) colorChooser.findViewById(R.id.sbAlpha);
		// SeekBar action listener
		seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateBackground();
			}
			
			//upgrades view background color from seekBar values
			private void updateBackground() {
				colorTest.setBackgroundColor(Color.argb(alphaBar.getProgress(),
						redBar.getProgress(), greenBar.getProgress(),
						blueBar.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		};
		//adds actionListener to seekbars
		redBar.setOnSeekBarChangeListener(seekBarChangeListener);
		greenBar.setOnSeekBarChangeListener(seekBarChangeListener);
		blueBar.setOnSeekBarChangeListener(seekBarChangeListener);
		alphaBar.setOnSeekBarChangeListener(seekBarChangeListener);
		//inflates view that is used to test color
		colorTest = colorChooser.findViewById(R.id.testColor);
		//creates actionlistener for preferences
		myListener = new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Dialog d = new Dialog(PrefsExtraRow.this);
				if (preference.getKey().equals("erBackgroundColor")) {
					d.setContentView(colorChooser);
					d.setCancelable(true);
					d.setTitle(R.string.titleBackgroundColor);
					d.setCanceledOnTouchOutside(true);
					d.show();
				} else if (preference.getKey().equals("erBtnBackground")) {
					d.setContentView(colorChooser);
					d.setCancelable(true);
					d.setTitle(R.string.titleBackgroundColor);
					d.setCanceledOnTouchOutside(true);
					d.show();
				}
				return false;
			}
		};
		//adds actionListeners for preferences
		findPreference("erBackgroundColor").setOnPreferenceClickListener(myListener);
		findPreference("erBtnBackground").setOnPreferenceClickListener(myListener);

	}

}
