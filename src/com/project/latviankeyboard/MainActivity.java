package com.project.latviankeyboard;

import java.util.ArrayList;

import com.project.latviankeyboard.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;

public class MainActivity extends Activity {
	
	ListView list;
	String[] listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listItems = new String[4];
		listItems[0] = this.getString(R.string.titleKeyboard1);
		listItems[1] = this.getString(R.string.titleKeyboard2);
		listItems[2] = this.getString(R.string.titleKeyboard3);
		listItems[3] = this.getString(R.string.titleAbout);
		list = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		list.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		
		
		
	}

}
