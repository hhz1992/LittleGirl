package com.tjpu;

import java.util.HashMap;
import java.util.Map;
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

public class ForgetPwdActivity extends Activity {
    /** Called when the activity is first created. */
    private String username;
    private String email;
    ProgressDialog p;
	SQLiteDatabase db;
	String newpwd;
//	ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpwd);	
      //创建或打开数据库（此处需要使用绝对路径）
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);		        
		Button forgetbutton=(Button)findViewById(R.id.forgetbutton);
		forgetbutton.setOnClickListener(new ForgetPwdListener());
		
    }    
    class ForgetPwdListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.username);
			username=edit1.getText().toString();

		    final EditText edit3=(EditText)findViewById(R.id.email);
		    email=edit3.getText().toString();
		    
		    if(username==null||username.equals("")||
		    		email==null||email.equals(""))
		    {
		    	Toast.makeText(ForgetPwdActivity.this, "输入不能为空！修改密码失败！",
						30000).show();
		    	return;
		    }else if(!util.forgetpwdtest(username,email,db))
		    {
		    	Toast.makeText(ForgetPwdActivity.this, "用户名或邮箱不匹配！操作失败！",
						30000).show();
		    	return;
		    }	
		    p = ProgressDialog.show(ForgetPwdActivity.this, "", "密码找回，正在向你发送邮件，请勿强行终止+...");
			Thread thread = new Thread(new Runnable() {
				
				public void run() {
					try {
						//修改密码
						Cursor cursor = db.rawQuery("select * from infomation", null);
						newpwd=util.getRandom(8);
						String oldpwd=util.findpwd(db);
						util.dencryptcoursor(cursor,oldpwd,util.encrytMD5(newpwd),db);
						util.modifyPwd(db,username,util.encrytMD5(newpwd));
						util.jiemiFiles(oldpwd);
						util.jiamiFiles(util.encrytMD5(newpwd));
						// 这个类主要是设置邮件
						MailSenderInfo mailInfo = new MailSenderInfo();
						mailInfo.setMailServerHost("smtp.126.com");
						mailInfo.setMailServerPort("25");
						mailInfo.setValidate(true);
						mailInfo.setUserName("littlegirlmaster@126.com");
						mailInfo.setPassword("littlegirl");// 您的邮箱密码
						mailInfo.setFromAddress("littlegirlmaster@126.com");
						String mailll=util.findemail(db);
						mailInfo.setToAddress(util.findemail(db));
						mailInfo.setSubject("Little-Girl忘记密码找回");
						mailInfo.setContent("你在LITTLE-GIRL的新密码为:"+newpwd);
						// 这个类主要来发送邮件
						SimpleMailSender sms = new SimpleMailSender();
						sms.sendTextMail(mailInfo);
					}catch (Exception e) {
				    
					}finally {
						p.dismiss();
				    	Intent intent=new Intent();	
				    	intent.setClass( ForgetPwdActivity.this,WelcomeActivity.class);
				    	ForgetPwdActivity.this.startActivity(intent);
				    	ForgetPwdActivity.this.finish();
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