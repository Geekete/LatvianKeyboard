package com.project.latviankeyboard;

import com.project.latviankeyboard.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	
	ListView list;
	String[] listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listItems = new String[4];
		listItems[0] = this.getString(R.string.titleExtraRowKeyboard);
		listItems[1] = this.getString(R.string.titleAltKeyKeyboard);
		listItems[2] = this.getString(R.string.titleKeyboard3);
		listItems[3] = this.getString(R.string.titleAbout);
		list = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		list.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		list.setOnItemClickListener(new OnItemClickListener() {
				


			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Log.i("ListView", "" + id);
				if (id == 0) {
					Intent intent = new Intent("com.project.latviankeyboard.PREFSEXTRAROW");   
					startActivity(intent);
				}		
			}
		});
	}
	
		

}


