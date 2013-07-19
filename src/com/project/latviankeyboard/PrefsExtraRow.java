package com.project.latviankeyboard;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class PrefsExtraRow extends PreferenceActivity {
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_extra_row);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i("PrefList", "pos: "+position+" id: "+id);
	}

	
	
}
