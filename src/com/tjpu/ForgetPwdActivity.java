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
      //����������ݿ⣨�˴���Ҫʹ�þ���·����
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
		    	Toast.makeText(ForgetPwdActivity.this, "���벻��Ϊ�գ��޸�����ʧ�ܣ�",
						30000).show();
		    	return;
		    }else if(!util.forgetpwdtest(username,email,db))
		    {
		    	Toast.makeText(ForgetPwdActivity.this, "�û��������䲻ƥ�䣡����ʧ�ܣ�",
						30000).show();
		    	return;
		    }	
		    p = ProgressDialog.show(ForgetPwdActivity.this, "", "�����һأ��������㷢���ʼ�������ǿ����ֹ+...");
			Thread thread = new Thread(new Runnable() {
				
				public void run() {
					try {
						//�޸�����
						Cursor cursor = db.rawQuery("select * from infomation", null);
						newpwd=util.getRandom(8);
						String oldpwd=util.findpwd(db);
						util.dencryptcoursor(cursor,oldpwd,util.encrytMD5(newpwd),db);
						util.modifyPwd(db,username,util.encrytMD5(newpwd));
						util.jiemiFiles(oldpwd);
						util.jiamiFiles(util.encrytMD5(newpwd));
						// �������Ҫ�������ʼ�
						MailSenderInfo mailInfo = new MailSenderInfo();
						mailInfo.setMailServerHost("smtp.126.com");
						mailInfo.setMailServerPort("25");
						mailInfo.setValidate(true);
						mailInfo.setUserName("littlegirlmaster@126.com");
						mailInfo.setPassword("littlegirl");// ������������
						mailInfo.setFromAddress("littlegirlmaster@126.com");
						String mailll=util.findemail(db);
						mailInfo.setToAddress(util.findemail(db));
						mailInfo.setSubject("Little-Girl���������һ�");
						mailInfo.setContent("����LITTLE-GIRL��������Ϊ:"+newpwd);
						// �������Ҫ�������ʼ�
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
		//�˳�����ʱ�ر�SQLiteDatabase
		if (db != null && db.isOpen())
		{
			db.close();
		}
	}
}