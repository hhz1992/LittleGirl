package com.tjpu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tjpu.R;
import com.tjpu.JiamiWenbenActivity.Button2Listener;
import com.tjpu.JiamiWenbenActivity.IndexListener;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class JiemiWenbenActivity extends Activity {
    /** Called when the activity is first created. */
	SQLiteDatabase db;
	ListView listView;
	Button delete=null;
	CheckBox cb=null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiemiwenben);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
    			.toString() + "/myinfo.db3" , null);	
        listView = (ListView)findViewById(R.id.show);
        Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
        delete=(Button)findViewById(R.id.deletecontent);
        delete.setOnClickListener(new DeleteListener());
        
        Button modifycontent=(Button)findViewById(R.id.modifycontent);
        modifycontent.setOnClickListener(new ModifycontentListener());
        
        TextView tjiami=(TextView)findViewById(R.id.tjiami); 
        tjiami.setOnClickListener(new TjiamiListener());
        listView.setCacheColorHint(Color.TRANSPARENT);
        String password=util.findpwd(db);	   
        try 
		{
			Cursor cursor = db.rawQuery("select * from infomation", null);
			inflateList(cursor,password);
		}
		catch(SQLiteException  se)
		{
			//执行DDL创建数据表
			db.execSQL("create table infomation(_id integer primary key autoincrement,"
				+ " inforname varchar(20),"
				+ " inforcontent varchar(50))");
			Cursor cursor = db.rawQuery("select * from infomation", null);
			inflateList(cursor,password);
		}
        
        
    }
    private List<Map<String, Object>> dencryptcoursor(Cursor cursor,String password)
    {
    	 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
    	{
    		try{
    		Map<String, Object> map = new HashMap<String, Object>();
    		String inforcontent=cursor.getString(2);
    		String inforname=cursor.getString(1);
    		byte[] decryptFrom = AESMethod.parseHexStr2Byte(inforcontent);  
    		byte[] decryptResult = AESMethod.decrypt(decryptFrom,password);  
    	    String dcontent=new String(decryptResult);
    	    map.put("inforname",inforname);
            map.put("inforcontent",dcontent );
            map.put("choice","");
            list.add(map);  
    		}
    		catch (Exception e){
    			continue;
    		}
          }
    	return list;
    	
    }
   
    private void inflateList(Cursor cursor,String password)
	{
    	List<Map<String, Object>> list=dencryptcoursor(cursor,password);
		SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.subshow,
				new String[]{"inforname","inforcontent","choice"},new int[]{R.id.myinforname,R.id.myinforcontent,R.id.myinforcheck});
		listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
	 class DeleteListener implements OnClickListener{

			@Override
				 public void onClick(View v)
		    	  {
				    for (int i = 0; i < listView.getChildCount(); i++) {
				    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// 获得子级
				    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.myinforcheck);// 从子级中获得控件
				    	TextView name=(TextView) ll.findViewById(R.id.myinforname);
				    	TextView content=(TextView) ll.findViewById(R.id.myinforcontent);
				    	String strname=(String) name.getText();
				    	String strcontent=(String) content.getText();
				    	if(chkone.isChecked()){			    		
				    		deleteinfor(strname,strcontent);
				    	}
				    }	
				    Intent intent=new Intent();	
					intent.putExtra("password","");
					intent.setClass( JiemiWenbenActivity.this,JiemiWenbenActivity.class);
					JiemiWenbenActivity.this.startActivity(intent);
					JiemiWenbenActivity.this.finish();
		    	  }
			}
	 public void deleteinfor(String strname,String strcontent){
		 String[] str={strname};
		int i=db.delete("infomation","inforname like ?",str);
		 
	 }
	 class TjiamiListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();	
				intent.setClass( JiemiWenbenActivity.this,JiamiWenbenActivity.class);
				JiemiWenbenActivity.this.startActivity(intent);
				JiemiWenbenActivity.this.finish();
			}
	    	
	    }
	 class IndexListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    final EditText userpassword=(EditText)findViewById(R.id.pwd);
				Intent intent=new Intent();	
				intent.setClass( JiemiWenbenActivity.this,MenuActivity.class);
				JiemiWenbenActivity.this.startActivity(intent);
				JiemiWenbenActivity.this.finish();
			}
	    	
	    }
	 class ModifycontentListener implements OnClickListener{

			@Override
		public void onClick(View v)
		    {
					String strname=null;
					String strcontent=null;
			    for (int i = 0; i < listView.getChildCount(); i++) {
			    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// 获得子级
			    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.myinforcheck);// 从子级中获得控件
			    	TextView name=(TextView) ll.findViewById(R.id.myinforname);
			    	TextView content=(TextView) ll.findViewById(R.id.myinforcontent);
			    	strname=(String) name.getText();
			    	strcontent=(String) content.getText();
			    	if(chkone.isChecked()){	
			    		break;
			    		}
			    	}	
				    Intent intent=new Intent();	
					intent.putExtra("inforname",strname);
					intent.putExtra("inforcontent",strcontent);
					intent.setClass( JiemiWenbenActivity.this,JiamiWenbenActivity.class);
					JiemiWenbenActivity.this.startActivity(intent);
					JiemiWenbenActivity.this.finish();
		    	  }
			}
	 
	 
}