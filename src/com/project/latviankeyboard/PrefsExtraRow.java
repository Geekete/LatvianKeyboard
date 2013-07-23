package com.project.latviankeyboard;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

public class PrefsExtraRow extends PreferenceActivity {

	OnPreferenceClickListener myListener;
	SeekBar redBar, greenBar, blueBar, alphaBar;
	View colorTest;
	LinearLayout colorChooser;
	SeekBar.OnSeekBarChangeListener seekBarChangeListener;
	Button btnOk, btnCancel;
	Dialog d;
	SharedPreferences prefs;
	String prefKey;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// inflates preferences
		addPreferencesFromResource(R.xml.prefs_extra_row);

		// inflates layout
		colorChooser = (LinearLayout) getLayoutInflater().inflate(
				R.layout.color_chooser, null);

		// gets shared preferences to edit them later
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = prefs.edit();

		// gets seekbars
		redBar = (SeekBar) colorChooser.findViewById(R.id.sbRed);
		greenBar = (SeekBar) colorChooser.findViewById(R.id.sbGreen);
		blueBar = (SeekBar) colorChooser.findViewById(R.id.sbBlue);
		alphaBar = (SeekBar) colorChooser.findViewById(R.id.sbAlpha);

		// inflates ok and cancel buttons
		btnOk = (Button) colorChooser.findViewById(R.id.btnOk);
		btnCancel = (Button) colorChooser.findViewById(R.id.btnCancel);

		// Sets EditTextPref values
		EditTextPreference verticalHeight = (EditTextPreference) findPreference("erVerticalHeight");
		verticalHeight.setText("" + prefs.getString("erVerticalHeight", "50"));
		EditTextPreference horizontalHeight = (EditTextPreference) findPreference("erHorizontalHeight");
		horizontalHeight.setText("" + prefs.getString("erHorizontalHeight", "60"));
		EditTextPreference waitTime = (EditTextPreference) findPreference("erWaitTime");
		waitTime.setText("" + prefs.getString("erWaitTime", "200"));
		EditTextPreference textSize = (EditTextPreference) findPreference("erTextSize");
		textSize.setText("" + prefs.getString("erTextSize", "25"));
		EditTextPreference buttonPadding = (EditTextPreference) findPreference("erBtnPadding");
		buttonPadding.setText("" + prefs.getString("erBtnPadding", "1"));

		// inflates Dialog context
		d = new Dialog(PrefsExtraRow.this);
		d.setContentView(colorChooser);
		d.setCancelable(true);
		d.setTitle(R.string.titleBackgroundColor);
		d.setCanceledOnTouchOutside(true);

		// SeekBar action listener
		seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateBackground();
			}

			// upgrades view background color from seekBar values
			private void updateBackground() {
				colorTest.setBackgroundColor(Color.argb(alphaBar.getProgress(), redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		};

		// adds actionlistener to "Ok" button
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editor.putString(prefKey, ""+Color.argb(alphaBar.getProgress(),
						redBar.getProgress(), greenBar.getProgress(),
						blueBar.getProgress()));
				editor.commit();
				d.dismiss();
			}
		});

		// adds actionListener to "Cancel" button
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				d.dismiss();
			}
		});

		// adds actionListener to seekbars
		redBar.setOnSeekBarChangeListener(seekBarChangeListener);
		greenBar.setOnSeekBarChangeListener(seekBarChangeListener);
		blueBar.setOnSeekBarChangeListener(seekBarChangeListener);
		alphaBar.setOnSeekBarChangeListener(seekBarChangeListener);

		// inflates view that is used to test color
		colorTest = colorChooser.findViewById(R.id.testColor);

		// creates actionlistener for preferences
		myListener = new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				prefKey = preference.getKey();
				if (prefKey.equals("erBackgroundColor")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 30, 30, 30)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();
				} else if (prefKey.equals("erBtnBackground")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 60, 60, 60)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();
				} else if (prefKey.equals("erBtnHoverColor")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 80, 80, 80)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();
				} else if (prefKey.equals("erBtnBorderColor")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 51, 181, 229)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();
				} else if (prefKey.equals("erBtnTextColor")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 255, 255, 255)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();
				/*} else if (prefKey.equals("erBtnRoundness")) {
					int bkColor = Integer.parseInt( prefs.getString(prefKey, ""+Color.argb(255, 30, 30, 30)) );
					colorTest.setBackgroundColor(bkColor);
					redBar.setProgress(Color.red(bkColor));
					greenBar.setProgress(Color.green(bkColor));
					blueBar.setProgress(Color.blue(bkColor));
					alphaBar.setProgress(Color.alpha(bkColor));
					d.show();*/
				} else if (prefKey.equals("callKeyboard")) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
							InputMethodManager.HIDE_NOT_ALWAYS);
				} else if (prefKey.equals("resetDefaults")) {
					editor.remove("erIsHapticOn");
					editor.remove("erVerticalHeight");
					editor.remove("erHorizontalHeight");
					editor.remove("erWaitTime");
					editor.remove("erBackgroundColor");
					editor.remove("erBtnBackground");
					editor.remove("erBtnHoverColor");
					editor.remove("erBtnBorderColor");
					editor.remove("erBtnTextColor");
					editor.remove("erTextSize");
					editor.remove("erBtnPadding");
					editor.remove("erBtnRoundness");
					editor.commit();
				}
				return false;
			}
		};

		// adds actionListeners for preferences
		findPreference("erBackgroundColor").setOnPreferenceClickListener(
				myListener);
		findPreference("erBtnBackground").setOnPreferenceClickListener(
				myListener);
		findPreference("erBtnHoverColor").setOnPreferenceClickListener(
				myListener);
		findPreference("erBtnBorderColor").setOnPreferenceClickListener(
				myListener);
		findPreference("erBtnTextColor").setOnPreferenceClickListener(
				myListener);
		findPreference("erBtnRoundness").setOnPreferenceClickListener(
				myListener);
		findPreference("callKeyboard").setOnPreferenceClickListener(myListener);
		findPreference("resetDefaults")
				.setOnPreferenceClickListener(myListener);

	}
}
