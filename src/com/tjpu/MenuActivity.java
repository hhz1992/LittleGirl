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
      //创建或打开数据库（此处需要使用绝对路径）
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
					//查看文本
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,JiemiWenbenActivity.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==1){
					//加密文本
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,JiamiWenbenActivity.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==2){
					//加密文件
					Intent intent=new Intent();	
					intent.setClass( MenuActivity.this,SDFileExplorer.class);
					MenuActivity.this.startActivity(intent);
				}else if(position==3){
					//解密文件
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
			listItem.put("title","查看文本信息");	
			listItem.put("content","查看已通过加密的所有文本内容");
			listItem.put("protect","little-girl:风雨无阻 一生相守");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo2);
			listItem.put("title","添加文本信息");	
			listItem.put("content","添加需要加密的文本内容");
			listItem.put("protect","little-girl:双重加密 安全可靠");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo4);
			listItem.put("title","加密文件");	
			listItem.put("content","浏览文件夹，选择需要加密的文件");
			listItem.put("protect","little-girl:诚心诚意 用心服务");
		listItems.add(listItem);
			listItem=new HashMap<String, Object>();
			listItem.put("icon", R.drawable.titlelogo3);
			listItem.put("title","解密文件");	
			listItem.put("content","将加密后的文件解密出来");
			listItem.put("protect","little-girl:追求卓越 永不止步");
		listItems.add(listItem);
	
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.menulist, new String[] { "icon", "title","content","protect" }, new int[] {
				R.id.menulogo, R.id.menutitle ,R.id.menucontent,R.id.protecttitle});
		listView.setAdapter(simpleAdapter);
	}
   
}