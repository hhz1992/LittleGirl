package com.tjpu;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.Toast;

public class RegisterActivity extends Activity {
    /** Called when the activity is first created. */
    private String username;
    private String password;
    private String email;
	SQLiteDatabase db;
//	ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);	
      //创建或打开数据库（此处需要使用绝对路径）
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);		        
		Button button1=(Button)findViewById(R.id.regist);
		button1.setOnClickListener(new Button1Listener());
		
    }
    class Button1Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.username);
			username=edit1.getText().toString();
		    final EditText edit2=(EditText)findViewById(R.id.pwd);
		    password=edit2.getText().toString();
		    final EditText edit3=(EditText)findViewById(R.id.email);
		    email=edit3.getText().toString();
			try 
			{
				//Cursor cursor = db.rawQuery("select * from usertable where username="+username, null);
				if(!isregist()){
					String sss=util.encrytMD5(password);
					insertData(db , username , util.encrytMD5(password),email);
					Intent intent=new Intent();	
					intent.setClass( RegisterActivity.this,LoginActivity.class);
					RegisterActivity.this.startActivity(intent);
				}
				Toast.makeText(RegisterActivity.this, "你已经注册过账号！",
						30000).show();
			}
			catch(SQLiteException  se)
			{
				//执行DDL创建数据表
				db.execSQL("create table usertable(_id integer primary key autoincrement,"
					+ " username varchar(20),"
					+ " password varchar(50),"
					+ " email varchar(20))");
				//执行insert语句插入数据
				insertData(db , username ,util.encrytMD5(password),email);
				Intent intent=new Intent();	
				intent.setClass( RegisterActivity.this,LoginActivity.class);
				RegisterActivity.this.startActivity(intent);

			}
			Intent intent=new Intent();	
			intent.setClass( RegisterActivity.this,LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
		}
    	
    }
 
	private void insertData(SQLiteDatabase db, String username , String password ,String email)
	{
		//执行插入语句
		
		db.execSQL("insert into usertable values(null , ? , ?, ?)", new String[]{username , password,email});
	}
	 private Boolean isregist(){
		 try{
	    		String sql="select * from usertable";
	    		Cursor cursor = db.rawQuery(sql, null);
	    		if(cursor!=null){
	    			return true;
	    		}else{
	    			return false;
	    		}  
		 }catch (Exception e){
			 return false;
		 }
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