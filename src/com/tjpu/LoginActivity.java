package com.tjpu;

import java.util.HashMap;
import java.util.Map;

import com.tjpu.R;
import com.tjpu.JiamiWenbenActivity.Button2Listener;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
    private String username;
    private String password;
	SQLiteDatabase db;
//	ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);	
      //创建或打开数据库（此处需要使用绝对路径）
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);		        
		Button button1=(Button)findViewById(R.id.login);
		button1.setOnClickListener(new Button1Listener());
		Button button2=(Button)findViewById(R.id.regist);
		button2.setOnClickListener(new Button2Listener());
		TextView modifypwd=(TextView)findViewById(R.id.modifypwd); 
		modifypwd.setOnClickListener(new ModifypwdListener());
		TextView forgetpwd=(TextView)findViewById(R.id.forgetpwd); 
		forgetpwd.setOnClickListener(new ForgetpwdListener());
		Button help=(Button)findViewById(R.id.help);
		help.setOnClickListener(new HelpListener());
		
    }
    class Button2Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( LoginActivity.this,RegisterActivity.class);
			LoginActivity.this.startActivity(intent);
		}
    	
    }
    class HelpListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( LoginActivity.this,HelpActivity.class);
			LoginActivity.this.startActivity(intent);
		}
    	
    }
    class ModifypwdListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( LoginActivity.this,ModifyPwdActivity.class);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}
    	
    }
    class ForgetpwdListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    
			Intent intent=new Intent();	
			intent.setClass( LoginActivity.this,ForgetPwdActivity.class);
			LoginActivity.this.startActivity(intent);
		}
    	
    }      
    class Button1Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.username);
			username=edit1.getText().toString();
		    final EditText edit2=(EditText)findViewById(R.id.pwd);
		    password=edit2.getText().toString();
		   
		    	if(util.userlogin(username,util.encrytMD5(password),db)){	
		    		Intent intent=new Intent();	
		    		intent.setClass( LoginActivity.this,MenuActivity.class);
		    		LoginActivity.this.startActivity(intent);
		    		LoginActivity.this.finish();
		    	}else{
					Toast.makeText(LoginActivity.this, "账号或密码错误！登陆失败！",
							30000).show();
		    	}
		}
    	
    }
    
	private void insertData(SQLiteDatabase db, String username , String password)
	{
		//执行插入语句
		db.execSQL("insert into usertable values(null , ? , ?)", new String[]{username , password});
	}
	@Override
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