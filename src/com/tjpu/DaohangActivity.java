package com.tjpu;

import java.util.HashMap;
import java.util.Map;

import com.tjpu.R;
import com.tjpu.JiemiWenbenActivity.TjiamiListener;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DaohangActivity extends Activity {
    /** Called when the activity is first created. */
    private String username;
    private String password;
	SQLiteDatabase db;
//	ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daohang);	
      //创建或打开数据库（此处需要使用绝对路径）
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);		        
		Button button1=(Button)findViewById(R.id.daohang1);
		button1.setOnClickListener(new ImageView1Listener());
		Button button2=(Button)findViewById(R.id.daohang2);
		button2.setOnClickListener(new ImageView2Listener());
		
    }
    class ImageView1Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( DaohangActivity.this,DaohangActivity.class);
			DaohangActivity.this.startActivity(intent);
		}
    	
    }
    class ImageView2Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( DaohangActivity.this,LoginActivity.class);
			DaohangActivity.this.startActivity(intent);
		}
    	
    }
}