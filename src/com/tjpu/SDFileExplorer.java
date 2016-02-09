package com.tjpu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tjpu.JiamiWenbenActivity.IndexListener;
import com.tjpu.JiemiWenbenActivity.DeleteListener;
import com.tjpu.JiemiWenbenActivity.TjiamiListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SDFileExplorer extends Activity
{
	SQLiteDatabase db;
	ListView listView;
	TextView textView;
	Button jiamibu;
	// 记录当前的父文件夹
	File currentParent;
	// 记录当前路径下的所有文件的文件数组
	File[] currentFiles;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdfileexplorer);
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
    			.toString() + "/myinfo.db3" , null);
		//获取列出全部文件的ListView
		listView = (ListView) findViewById(R.id.list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
		textView = (TextView) findViewById(R.id.path);
		TextView tjiemi=(TextView)findViewById(R.id.twjiemi); 
        tjiemi.setOnClickListener(new TjiemiListener());
        jiamibu=(Button)findViewById(R.id.jiami);
        jiamibu.setOnClickListener(new JiamiListener());
		//获取系统的SD卡的目录	
		String dest=null;
		try {
			File sdCardDir=Environment.getExternalStorageDirectory();
			dest=sdCardDir.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File root = new File(dest);
		//如果 SD卡存在
		if (root.exists())
		{
			currentParent = root;
			currentFiles = root.listFiles();
			//使用当前目录下的全部文件、文件夹来填充ListView
			inflateListView(currentFiles);
		}

		// 为ListView的列表项的单击事件绑定监听器
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id)
			{
				// 用户单击了文件,则加密文件
				if (currentFiles[position].isFile())
				{
					return;
				}
				// 获取用户点击的文件夹下的所有文件
				else{
					File[] tmp = currentFiles[position].listFiles();
					if (tmp == null || tmp.length == 0)
					{
						Toast.makeText(SDFileExplorer.this, "当前路径不可访问或该路径下没有文件！",
						30000).show();
					}else if(currentFiles[position].getName().equals("jiamiwenjian")){
						Toast.makeText(SDFileExplorer.this, "系统文件夹，不允许读取！",
								30000).show();
					}
					else
					{
					//获取用户单击的列表项对应的文件夹，设为当前的父文件夹
					currentParent = currentFiles[position];
					//保存当前的父文件夹内的全部文件和文件夹
					currentFiles = tmp;
					// 再次更新ListView
					inflateListView(currentFiles);
					}
				}
			}
		});
		// 获取上一级目录的按钮
		Button parent = (Button) findViewById(R.id.parent);
		parent.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View source)
			{
				try
				{
					
					if (!currentParent.getCanonicalPath().equals("/sdcard"))
					{
						// 获取上一级目录
						currentParent = currentParent.getParentFile();
						// 列出当前目录下所有文件
						currentFiles = currentParent.listFiles();
						// 再次更新ListView
						inflateListView(currentFiles);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
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
			listItem.put("check","");		
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.line, new String[] { "icon", "fileName","check" }, new int[] {
				R.id.icon, R.id.file_name ,R.id.checkwenjian});
		// 为ListView设置Adapter
		listView.setAdapter(simpleAdapter);
		try
		{
			textView.setText("当前路径为：" + currentParent.getCanonicalPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	 class TjiemiListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();	
				intent.setClass( SDFileExplorer.this,SDFileJiemi.class);
				SDFileExplorer.this.startActivity(intent);
				SDFileExplorer.this.finish();
			}
	    	
	    }
	 class IndexListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();	
				intent.setClass( SDFileExplorer.this,MenuActivity.class);
				SDFileExplorer.this.startActivity(intent);
				SDFileExplorer.this.finish();
			}
	    	
	    }
	 
		
	 class JiamiListener implements OnClickListener{

			@Override
		 public void onClick(View v)
		    {
			final ProgressDialog p= ProgressDialog.show(SDFileExplorer.this, "", "正在加密文件,请勿强行中断...");
				
				Thread thread = new Thread(new Runnable() {
					public void run() {
					try {
						for (int i = 0; i < listView.getChildCount(); i++) {
					    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// 获得子级
					    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.checkwenjian);// 从子级中获得控件
					    	if(chkone.isChecked()&&currentFiles[i].isFile()&&FileAESMethod.allowfile(currentFiles[i].getName())){			    		
					    			String password=util.findpwd(db);
									String strr=currentParent.getCanonicalPath()+"/"+currentFiles[i].getName();									
									FileAESMethod.encryptfile(password,strr,currentFiles[i].getName());
									File file=new File(strr);
									file.delete();
					    	}
						}
					}
					catch (Exception e) {
						
					} finally {
						p.dismiss();
				    	Intent intent=new Intent();	
				    	intent.setClass( SDFileExplorer.this,SDFileExplorer.class);
				    	SDFileExplorer.this.startActivity(intent);
				    	SDFileExplorer.this.finish();
					}
				}
			});
			thread.start();

		}
	}
}