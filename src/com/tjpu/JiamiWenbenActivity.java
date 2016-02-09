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

public class JiamiWenbenActivity extends Activity {
    /** Called when the activity is first created. */
    private String name;
    private String content;
	SQLiteDatabase db;
//	ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiamicontent);	
      //����������ݿ⣨�˴���Ҫʹ�þ���·����
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
			.toString() + "/myinfo.db3" , null);
		//��������Ϊ�޸İ�ť����Ҫ��ȡ��������ֵ
		Intent intent=getIntent();
        String name=intent.getStringExtra("inforname");
        String content=intent.getStringExtra("inforcontent");
        if(name!=null&&!name.equals("")&&content!=null&&!content.equals("")){
        	final EditText edit1=(EditText)findViewById(R.id.name);
		    final EditText edit3=(EditText)findViewById(R.id.content);
		    edit1.setText(name);
			edit3.setText(content);
        }
        
        Button modify=(Button)findViewById(R.id.modify);
        modify.setOnClickListener(new ModifyListener());
        
		Button jiami=(Button)findViewById(R.id.jiami);
		Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
		jiami.setOnClickListener(new Button1Listener());		
		TextView tjiemi=(TextView)findViewById(R.id.tjiemi);
		tjiemi.setOnClickListener(new Button2Listener());
		
    }
    
    class IndexListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();	
			intent.setClass( JiamiWenbenActivity.this,MenuActivity.class);
			JiamiWenbenActivity.this.startActivity(intent);
			JiamiWenbenActivity.this.finish();
		}
    	
    }
    class ModifyListener implements OnClickListener{

    	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.name);
			name=edit1.getText().toString();
		    final EditText edit3=(EditText)findViewById(R.id.content);
		    content=edit3.getText().toString();
		    String password=util.findpwd(db);			
	        byte[] encryptResult = AESMethod.encrypt(content, password);  
			String encryptResultStr = AESMethod.parseByte2HexStr(encryptResult);  
			try 
			{
				if(name==null||name.equals(""))
				{
					Toast.makeText(JiamiWenbenActivity.this, "���ⲻ��Ϊ�գ�",
							30000).show();
				}
				else if(content==null||content.equals(""))
				{
					Toast.makeText(JiamiWenbenActivity.this, "���ݲ���Ϊ�գ�",
							30000).show();
				}
				//�����������������ͬ������
				else if(util.findinforname(db).contains(name)){
					util.modifyData(db , name , encryptResultStr);
					Toast.makeText(JiamiWenbenActivity.this, "�޸ĳɹ���",
							30000).show();
					edit1.setText("");
					edit3.setText("");
				}else{
					insertData(db , name , encryptResultStr);
					Toast.makeText(JiamiWenbenActivity.this, "��������ͬ�ı��⣬���²������ݳɹ���",
							30000).show();
					edit1.setText("");
					edit3.setText("");
				
				}
					
				
			}
			catch(SQLiteException  se)
			{
				Toast.makeText(JiamiWenbenActivity.this, "�޸�ʧ�ܣ�",
						30000).show();
			}
		}
    	
    }
    class Button2Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();	
			intent.setClass( JiamiWenbenActivity.this,JiemiWenbenActivity.class);
			JiamiWenbenActivity.this.startActivity(intent);
			JiamiWenbenActivity.this.finish();
		}
    	
    }
    class Button1Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText edit1=(EditText)findViewById(R.id.name);
			name=edit1.getText().toString();
		    final EditText edit3=(EditText)findViewById(R.id.content);
		    content=edit3.getText().toString();
		    String password=util.findpwd(db);			
	        byte[] encryptResult = AESMethod.encrypt(content, password);  
			String encryptResultStr = AESMethod.parseByte2HexStr(encryptResult);  
			try 
			{
				if(name==null||name.equals(""))
				{
					Toast.makeText(JiamiWenbenActivity.this, "���ⲻ��Ϊ�գ�",
							30000).show();
				}
				else if(content==null||content.equals(""))
				{
					Toast.makeText(JiamiWenbenActivity.this, "���ݲ���Ϊ�գ�",
							30000).show();
				}
				//�����������������ͬ������
				else if(!util.findinforname(db).contains(name)){
					insertData(db , name , encryptResultStr);
					Toast.makeText(JiamiWenbenActivity.this, "���ܳɹ���",
							30000).show();
					edit1.setText("");
					edit3.setText("");
				}
					
				else Toast.makeText(JiamiWenbenActivity.this, "�ñ����Ѿ����ڣ���������⣡",
						30000).show();
			}
			catch(SQLiteException  se)
			{
				//ִ��DDL�������ݱ�
				db.execSQL("create table infomation(_id integer primary key autoincrement,"
					+ " inforname varchar(50),"
					+ " inforcontent varchar(200))");
				//ִ��insert����������
				insertData(db , name , encryptResultStr);
			}
		}
    	
    }

	private void insertData(SQLiteDatabase db, String title , String content)
	{
		//ִ�в������
		db.execSQL("insert into infomation values(null , ? , ?)", new String[]{title , content});
	}
	
	@Override
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