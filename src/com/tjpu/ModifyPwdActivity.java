package com.tjpu;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.tjpu.R;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class ModifyPwdActivity extends Activity {
    /** Called when the activity is first created. */
    private String username;
    private String password;
    private String newpassword;
    private String rnewpassword;
    ProgressDialog p;
	SQLiteDatabase db;
//	ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypwd);	
      //创建或打开数据库（此处需要使用绝对路径）
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);		        
		Button modifybutton=(Button)findViewById(R.id.modifypwd);
		modifybutton.setOnClickListener(new ModifyPwdListener());
		
    }    
    class ModifyPwdListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.username);
			username=edit1.getText().toString();
			final EditText edit2=(EditText)findViewById(R.id.pwd);
			password=edit2.getText().toString();
		    final EditText edit3=(EditText)findViewById(R.id.newpwd);
		    newpassword=edit3.getText().toString();
		    final EditText edit4=(EditText)findViewById(R.id.rnewpwd);
		    rnewpassword=edit4.getText().toString();
		    if(username==null||username.equals("")||
		    		password==null||password.equals("")||
		    		newpassword==null||newpassword.equals("")||
		    		rnewpassword==null||rnewpassword.equals(""))
		    {
		    	Toast.makeText(ModifyPwdActivity.this, "输入不能为空！修改密码失败！",
						30000).show();
		    	return;
		    }else if(!newpassword.equals(rnewpassword)){
		    	Toast.makeText(ModifyPwdActivity.this, "两次输入不一致！修改密码失败！",
						30000).show();
		    	return;
		    }else if(!util.userlogin(username,util.encrytMD5(password),db))
		    {
		    	Toast.makeText(ModifyPwdActivity.this, "原用户名或密码错误！修改密码失败！",
						30000).show();
		    	return;
		    }
		    p = ProgressDialog.show(ModifyPwdActivity.this, "", "正在修改密码,请勿强行中断...");
			Thread thread = new Thread(new Runnable() {
				
				public void run() {
				try {
					Cursor cursor = db.rawQuery("select * from infomation", null);
			    	util.dencryptcoursor(cursor,util.encrytMD5(password),util.encrytMD5(newpassword),db);
			    	util.modifyPwd(db,username,util.encrytMD5(newpassword));
			    	util.jiemiFiles(util.encrytMD5(password));
			    	util.jiamiFiles(util.encrytMD5(newpassword));
					
				} catch (Exception e) {
					System.out.println("mSend_OnClickListener="
							+ e.getMessage());
				} finally {
					p.dismiss();
			    	Intent intent=new Intent();	
			    	intent.setClass( ModifyPwdActivity.this,LoginActivity.class);
			    	ModifyPwdActivity.this.startActivity(intent);
			    	ModifyPwdActivity.this.finish();
				}
			}
		});
		thread.start();
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