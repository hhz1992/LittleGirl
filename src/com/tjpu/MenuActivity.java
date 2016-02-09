package com.tjpu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tjpu.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends Activity {
    /** Called when the activity is first created. */
	ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);	
      //����������ݿ⣨�˴���Ҫʹ�þ���·����
        listView = (ListView) findViewById(R.id.menulist);
        listView.setCacheColorHint(Color.TRANSPARENT);
        flateMenuListView();
        listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id)
			{
				if(position==0){
					//�鿴�ı�
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,JiemiWenbenActivity.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==1){
					//�����ı�
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,JiamiWenbenActivity.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==2){
					//�����ļ�
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,SDFileExplorer.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==3){
					//�����ļ�
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,SDFileJiemi.class);
					MenuActivity.this.startActivity(intent);
				}
			}
		});
    }
    
    public void flateMenuListView()
	{
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo1);
			listItem.put("title","�鿴�ı���Ϣ");	
			listItem.put("content","�鿴��ͨ�����ܵ������ı�����");
			listItem.put("protect","little-girl:�������� һ������");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo2);
			listItem.put("title","����ı���Ϣ");	
			listItem.put("content","�����Ҫ���ܵ��ı�����");
			listItem.put("protect","little-girl:˫�ؼ��� ��ȫ�ɿ�");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo4);
			listItem.put("title","�����ļ�");	
			listItem.put("content","����ļ��У�ѡ����Ҫ���ܵ��ļ�");
			listItem.put("protect","little-girl:���ĳ��� ���ķ���");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo3);
			listItem.put("title","�����ļ�");	
			listItem.put("content","�����ܺ���ļ����ܳ���");
			listItem.put("protect","little-girl:׷��׿Խ ����ֹ��");
		listItems.add(listItem);
	
		// ����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.menulist, new String[] { "icon", "title","content","protect" }, new int[] {
				R.id.menulogo, R.id.menutitle ,R.id.menucontent,R.id.protecttitle});
		listView.setAdapter(simpleAdapter);
	}
   
}