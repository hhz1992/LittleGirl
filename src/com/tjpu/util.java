package com.tjpu;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class util {
	public static String findpwd(SQLiteDatabase db){

		try{
		String sql="select * from usertable";
		System.out.println(sql);
		Cursor cursor = db.rawQuery(sql, null);
		String upwd=null;
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{      		
			upwd=cursor.getString(2);               	
		}         
		return upwd;
		}catch (Exception e){
			return null;
		}
	}
	public static String findemail(SQLiteDatabase db){

		try{
		String sql="select * from usertable";
		System.out.println(sql);
		Cursor cursor = db.rawQuery(sql, null);
		String upwd=null;
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{      		
			upwd=cursor.getString(3);               	
		}         
		return upwd;
		}catch (Exception e){
			return null;
		}
	}
	public static ArrayList<String> findinforname(SQLiteDatabase db){

		try{
		Cursor cursor = db.rawQuery("select * from infomation", null);
		ArrayList<String> inforname=new ArrayList<String>();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{      		
			inforname.add(cursor.getString(1));               	
		}         
			return inforname;
		}catch (Exception e){
			return null;
		}
	}
	//MD5加密
	public static String encrytMD5(String data){
		//指定加密算法
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			digest.update(data.getBytes());
			return encryptMD5toString(digest.digest());	
	}
	//将加密后的字节数组转化为固定长度的字符串
	public static String encryptMD5toString(byte[] data){
		String str="";
		String str16; 
		for(int i=0;i<data.length;i++){
			//转换为16进制数据
			//Integer.toHexString的参数是int，如果不进行&0xff，那么当一个byte会转换成int时，由于int是32位，而byte只有8位这时会进行补位，
			//例如补码11111111的十进制数为-1转换为int时变为11111111111111111111111111111111好多1啊，呵呵！即0xffffffff但是这个数是不对的，这种补位就会造成误差。
			//和0xff相与后，高24比特就会被清0了，结果就对了。
			str16=Integer.toHexString(0xFF & data[i]);
			if(str16.length()==1){
				str=str+"0"+str16;
     		}else{
     			str=str+str16;
     		}
		}
		return str;
	}
	static Boolean userlogin(String username,String password,SQLiteDatabase db){
    	try{
    		String sql="select * from usertable";
    		System.out.println(sql);
    		Cursor cursor = db.rawQuery(sql, null);
    		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        	{      		
        		try{
        			  String name=cursor.getString(1);
        			  String upwd=cursor.getString(2);               	
        			if(upwd!=null&&upwd.equals(password)&&username!=null&&username.equals(name)){
        				return true;
        			 }                	
        		 }
        		catch (Exception e){
        			return false;
        		}
        	}
    	}catch (Exception e){
    		return false;
    	}
		return false;
		
    }
	static Boolean forgetpwdtest(String username,String email,SQLiteDatabase db){
    	try{
    		String sql="select * from usertable";
    		System.out.println(sql);
    		Cursor cursor = db.rawQuery(sql, null);
    		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        	{      		
        		try{
        			  String name=cursor.getString(1);
        			  String dbemail=cursor.getString(3);               	
        			if(username!=null&&name.equals(username)&&email!=null&&email.equals(dbemail)){
        				return true;
        			 }                	
        		 }
        		catch (Exception e){
        			return false;
        		}
        	}
    	}catch (Exception e){
    		return false;
    	}
		return false;
		
    }
	static boolean dencryptcoursor(Cursor cursor,String oldpassword,String newpasswrod,SQLiteDatabase db)
    {
    	for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
    	{
    		try{
    		Map<String, String> map = new HashMap<String, String>();
    		String inforcontent=cursor.getString(2);
    		String inforname=cursor.getString(1);
    		//解密
    		byte[] decryptFrom = AESMethod.parseHexStr2Byte(inforcontent);  
    		byte[] decryptResult = AESMethod.decrypt(decryptFrom,oldpassword);  
    	    String dcontent=new String(decryptResult);
    	    //加密
    	    byte[] encryptResult = AESMethod.encrypt(dcontent, newpasswrod);  
			String encryptResultStr = AESMethod.parseByte2HexStr(encryptResult);
			//更新数据
			util.modifyData(db , inforname , encryptResultStr);
			
    		}
    		catch (Exception e){
    			continue;
    		}
          }
		return true;    	
    }
	static void modifyData(SQLiteDatabase db, String title , String content)
	{
		//执行插入语句
		ContentValues values=new ContentValues();
		values.put("inforcontent",content);
		db.update("infomation" ,values,"inforname=?", new String[]{title});
	}
	 static void modifyPwd(SQLiteDatabase db, String username , String password)
	{
			//执行插入语句
			ContentValues values=new ContentValues();
			values.put("password",password);
			db.update("usertable" ,values,"username=?", new String[]{username});
	}
	 //加密文件下所有文件
	 static void jiamiFiles(String password)
		{
		 File sdCardDir=Environment.getExternalStorageDirectory();
			File file=null;
			try {
				file = new File(sdCardDir.getCanonicalPath()+"/little-girl");
				if (!file.exists()) {
					return;
				}
			} catch (IOException e1) {
				return;
			}
			File[] files=file.listFiles();
			for (int i = 0; i < files.length;i++)
			{
				if(files[i].isDirectory())
						continue;
				else{
					try {
						FileAESMethod.encryptfile(password,sdCardDir.getCanonicalPath()+"/little-girl/"+files[i].getName(),files[i].getName());
						File destoryfile=new File(sdCardDir.getCanonicalPath()+"/little-girl/"+files[i].getName());
						destoryfile.delete();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						continue;
					}
				}
			}
			return;
			
		}
	 //解密文件下所有文件
	 static void jiemiFiles(String password)
	 {
		 	File sdCardDir=Environment.getExternalStorageDirectory();
			File destDir=null;
			try {
				destDir = new File(sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian");
				if (!destDir.exists()) {
					return;
				}
			} catch (IOException e1) {
				return;
			}
			
			File[] filelist=destDir.listFiles();
			for (int i = 0; i < filelist.length;i++)
			{
				if(filelist[i].isDirectory())
						continue;
				else{
					try {
						 FileAESMethod.dencryptfile( password,filelist[i].getName(), filelist[i].getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						continue;
					}
				}
			}
			
		}
	 /*************************生成随机密码****************************/
	 public static String getRandom(int lenth) {
	 	   StringBuffer buffer = new StringBuffer(
	 	     "0123456789abcdefghijklmnopqrstuvwxyz");
	 	   StringBuffer sb = new StringBuffer();
	 	   Random r = new Random();
	 	   int range = buffer.length();
	 	   for (int i = 0; i < lenth; i++) {
	 	    //生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
	 	    sb.append(buffer.charAt(r.nextInt(range)));
	 	   }
	 	   return sb.toString();
	 	}
	
}
