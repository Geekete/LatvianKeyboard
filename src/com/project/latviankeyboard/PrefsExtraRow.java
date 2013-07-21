package com.project.latviankeyboard;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PrefsExtraRow extends PreferenceActivity {
	
	OnPreferenceClickListener myListener;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_extra_row);
		myListener = new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference preference) {
				LinearLayout colorChooser = (LinearLayout) getLayoutInflater().inflate(R.layout.color_chooser, null);
				Dialog d = new Dialog(PrefsExtraRow.this);
				if(preference.getKey().equals("erBackgroundColor")){
					d.setContentView(colorChooser);
					colorChooser.findViewById(R.id.sbRed);
					d.setCancelable(true);
					d.setTitle(R.string.titleBackgroundColor);
					d.show();				
					}
				return false;
			}
		};
		findPreference("erBackgroundColor").setOnPreferenceClickListener(myListener);
	}
	
}
