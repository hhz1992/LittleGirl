package com.tjpu;

import com.tjpu.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends Activity {
    /** Called when the activity is first created. */
    private String name;
    private String content;
	SQLiteDatabase db;
//	ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);	
		Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
	
		
    }
    
    class IndexListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();	
			intent.setClass( HelpActivity.this,LoginActivity.class);
			HelpActivity.this.startActivity(intent);
			HelpActivity.this.finish();
		}
    	
    }
	public void onDestroy()
	{
		super.onDestroy();
		//退出程序时关闭SQLiteDatabase
		if (db != null && db.isOpen())
		{
			db.close();
		}
	}
}