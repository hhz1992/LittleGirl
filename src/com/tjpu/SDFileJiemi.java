package com.tjpu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tjpu.JiamiWenbenActivity.IndexListener;
import com.tjpu.SDFileExplorer.TjiemiListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SDFileJiemi extends Activity
{
	SQLiteDatabase db;
	ListView listView;
	// ��¼��ǰ�ĸ��ļ���
	File currentParent;
	// ��¼��ǰ·���µ������ļ����ļ�����
	File[] currentFiles;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdfilejiemi);
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
    			.toString() + "/myinfo.db3" , null);
		//��ȡ�г�ȫ���ļ���ListView
		listView = (ListView) findViewById(R.id.list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		//��ȡϵͳ��SD����Ŀ¼	
		Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
		Button jiemi=(Button)findViewById(R.id.jiemi);
		jiemi.setOnClickListener(new JiemiListener());
		Button deletewenjian=(Button)findViewById(R.id.deletewenjian);
		deletewenjian.setOnClickListener(new DeleteListener());
		
		TextView twjiami=(TextView)findViewById(R.id.twjiami);
		twjiami.setOnClickListener(new TjiamiListener());
		String dest=null;
		try {
			File sdCardDir=Environment.getExternalStorageDirectory();
			dest=sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian";
			File destDir = new File(sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian");
			if (!destDir.exists()) {
 			   destDir.mkdirs();
 			  }
		} catch (IOException e1) {
			return;
		}
		File root = new File(dest);
		//��� SD������
		if (root.exists())
		{
			currentParent = root;
			currentFiles = root.listFiles();
			//ʹ�õ�ǰĿ¼�µ�ȫ���ļ����ļ��������ListView
			inflateListView(currentFiles);
		}
	}
	private void inflateListView(File[] files)
	{
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < files.length;i++)
		{
			Map<String, Object> listItem = new HashMap<String, Object>();
			if (files[i].isDirectory())
				listItem.put("icon", R.drawable.folder);
			else if(files[i].getName().endsWith(".txt"))
					 listItem.put("icon", R.drawable.txtimg);
			else if(files[i].getName().endsWith(".doc")||files[i].getName().endsWith(".docx"))
					listItem.put("icon", R.drawable.doc);
			else if(files[i].getName().endsWith(".xls")||files[i].getName().endsWith(".xlsx"))
					listItem.put("icon", R.drawable.xls);
			else if(files[i].getName().endsWith(".pdf"))
					listItem.put("icon", R.drawable.pdf);
			else if(files[i].getName().endsWith(".jpg"))
				listItem.put("icon", R.drawable.picture);
			else if(files[i].getName().endsWith(".mp3"))
				listItem.put("icon", R.drawable.mp3);
			else listItem.put("icon", R.drawable.weizhi);		
					listItem.put("fileName", files[i].getName());
			
			listItems.add(listItem);
		}
		// ����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.line, new String[] { "icon", "fileName" }, new int[] {
				R.id.icon, R.id.file_name });
		// ΪListView����Adapter
		listView.setAdapter(simpleAdapter);
		
	}
	 class IndexListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();	
				intent.setClass( SDFileJiemi.this,MenuActivity.class);
				SDFileJiemi.this.startActivity(intent);
				SDFileJiemi.this.finish();
			}
	    	
	    }
	 class TjiamiListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();	
				intent.setClass( SDFileJiemi.this,SDFileExplorer.class);
				SDFileJiemi.this.startActivity(intent);
				SDFileJiemi.this.finish();
			}
	    	
	    }
	 class JiemiListener implements OnClickListener{

			@Override
			 public void onClick(View v)
		    {
				final ProgressDialog p= ProgressDialog.show(SDFileJiemi.this, "", "���ڽ����ļ�,����ǿ���ж�...");
				Thread thread = new Thread(new Runnable() {
					public void run() 
					{
						try {
								for (int i = 0; i < listView.getChildCount(); i++) {
						    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// ����Ӽ�
						    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.checkwenjian);// ���Ӽ��л�ÿؼ�
						    	if(chkone.isChecked()&&currentFiles[i].isFile()){			    		
						    		String password=util.findpwd(db);
									FileAESMethod.dencryptfile(password,currentFiles[i].getName(),currentFiles[i].getName());
									}
						    	}
						    	
						}catch(Exception e){
							
						}finally {
							p.dismiss();
						}
					}
				});
				thread.start();
				Toast.makeText(SDFileJiemi.this, "���ܳɹ����뵽SD����little-girl�ļ����²鿴��",
						40000).show();
				}
			}

	 class DeleteListener implements OnClickListener{

			@Override
				 public void onClick(View v)
		    	  {
				    for (int i = 0; i < listView.getChildCount(); i++) {
				    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// ����Ӽ�
				    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.checkwenjian);// ���Ӽ��л�ÿؼ�
				    	if(chkone.isChecked()&&currentFiles[i].isFile()){			    		
				    		try {
									String filename=currentFiles[i].getName();
									File sdCardDir=Environment.getExternalStorageDirectory();
									File destDir = new File(sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian/"+filename);
									boolean b= currentFiles[i].delete();													
								} catch (Exception e) {
									e.printStackTrace();
								}
				    		}
					    }
				    	Toast.makeText(SDFileJiemi.this, "ɾ���ɹ���",30000).show();	
				    	Intent intent=new Intent();	
				    	intent.setClass( SDFileJiemi.this,SDFileJiemi.class);
				    	SDFileJiemi.this.startActivity(intent);
				    	SDFileJiemi.this.finish();
				    }
	 }
	 
}
			
