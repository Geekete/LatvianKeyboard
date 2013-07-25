package com.project.latviankeyboard;

import com.project.latviankeyboard.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

public class MainActivity extends Activity {
	
	ListView list;
	String[] listItems;
	Dialog dancingDialog;
	ScrollView dkLayout;
	TextView tvDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listItems = new String[4];
		listItems[0] = this.getString(R.string.titleExtraRowKeyboard);
		listItems[1] = this.getString(R.string.titleAltKeyKeyboard);
		listItems[2] = this.getString(R.string.titleDancingKeyboard);
		listItems[3] = this.getString(R.string.titleAbout);
		list = (ListView) findViewById(R.id.listView);
		dkLayout = (ScrollView) getLayoutInflater().inflate(R.layout.dancing_keyboard_dialog, null);
		dancingDialog = new Dialog(MainActivity.this);
		dancingDialog.setContentView(dkLayout);
		dancingDialog.setCanceledOnTouchOutside(true);
		tvDialog = (TextView) dkLayout.findViewById(R.id.tvDKeyboard);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		list.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		list.setOnItemClickListener(new OnItemClickListener() {
				


			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (id == 0) {
					Intent intent = new Intent("com.project.latviankeyboard.PREFSEXTRAROW");   
					startActivity(intent);
				} else if (id == 1) {
					Intent intent = new Intent("com.project.latviankeyboard.PREFSALTKEY");   
					startActivity(intent);
				} else if (id == 2) {
					tvDialog.setText(R.string.textDancingKeyboard);
					dancingDialog.show();
				} else {
					tvDialog.setText(R.string.textAbout);
					dancingDialog.show();
				}
			}
		});
	}
	
		

}


