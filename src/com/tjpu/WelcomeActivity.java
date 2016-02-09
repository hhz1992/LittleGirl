package com.tjpu;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.tjpu.R;

import android.app.Activity;
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

public class WelcomeActivity extends Activity {
    /** Called when the activity is first created. */
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);	
      //����������ݿ⣨�˴���Ҫʹ�þ���·����
		
		final Intent it = new Intent(this, DaohangActivity.class); //��Ҫת���Activity
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run() {
		    startActivity(it); //ִ��
		    WelcomeActivity.this.finish();
			}
		 };
		timer.schedule(task, 1000 * 3); //10���	
		
    }
   
}