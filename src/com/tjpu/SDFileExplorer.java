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
	// ��¼��ǰ�ĸ��ļ���
	File currentParent;
	// ��¼��ǰ·���µ������ļ����ļ�����
	File[] currentFiles;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdfileexplorer);
		db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
    			.toString() + "/myinfo.db3" , null);
		//��ȡ�г�ȫ���ļ���ListView
		listView = (ListView) findViewById(R.id.list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		Button index=(Button)findViewById(R.id.index);
		index.setOnClickListener(new IndexListener());
		textView = (TextView) findViewById(R.id.path);
		TextView tjiemi=(TextView)findViewById(R.id.twjiemi); 
        tjiemi.setOnClickListener(new TjiemiListener());
        jiamibu=(Button)findViewById(R.id.jiami);
        jiamibu.setOnClickListener(new JiamiListener());
		//��ȡϵͳ��SD����Ŀ¼	
		String dest=null;
		try {
			File sdCardDir=Environment.getExternalStorageDirectory();
			dest=sdCardDir.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

		// ΪListView���б���ĵ����¼��󶨼�����
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id)
			{
				// �û��������ļ�,������ļ�
				if (currentFiles[position].isFile())
				{
					return;
				}
				// ��ȡ�û�������ļ����µ������ļ�
				else{
					File[] tmp = currentFiles[position].listFiles();
					if (tmp == null || tmp.length == 0)
					{
						Toast.makeText(SDFileExplorer.this, "��ǰ·�����ɷ��ʻ��·����û���ļ���",
						30000).show();
					}else if(currentFiles[position].getName().equals("jiamiwenjian")){
						Toast.makeText(SDFileExplorer.this, "ϵͳ�ļ��У��������ȡ��",
								30000).show();
					}
					else
					{
					//��ȡ�û��������б����Ӧ���ļ��У���Ϊ��ǰ�ĸ��ļ���
					currentParent = currentFiles[position];
					//���浱ǰ�ĸ��ļ����ڵ�ȫ���ļ����ļ���
					currentFiles = tmp;
					// �ٴθ���ListView
					inflateListView(currentFiles);
					}
				}
			}
		});
		// ��ȡ��һ��Ŀ¼�İ�ť
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
						// ��ȡ��һ��Ŀ¼
						currentParent = currentParent.getParentFile();
						// �г���ǰĿ¼�������ļ�
						currentFiles = currentParent.listFiles();
						// �ٴθ���ListView
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
		// ����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.line, new String[] { "icon", "fileName","check" }, new int[] {
				R.id.icon, R.id.file_name ,R.id.checkwenjian});
		// ΪListView����Adapter
		listView.setAdapter(simpleAdapter);
		try
		{
			textView.setText("��ǰ·��Ϊ��" + currentParent.getCanonicalPath());
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
			final ProgressDialog p= ProgressDialog.show(SDFileExplorer.this, "", "���ڼ����ļ�,����ǿ���ж�...");
				
				Thread thread = new Thread(new Runnable() {
					public void run() {
					try {
						for (int i = 0; i < listView.getChildCount(); i++) {
					    	LinearLayout ll = (LinearLayout)listView.getChildAt(i);// ����Ӽ�
					    	CheckBox chkone = (CheckBox) ll.findViewById(R.id.checkwenjian);// ���Ӽ��л�ÿؼ�
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