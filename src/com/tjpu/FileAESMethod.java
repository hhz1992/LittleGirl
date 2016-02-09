package com.tjpu;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.os.Environment;

public class FileAESMethod {
	 // 加密文件
	 public static void encryptfile(String pwd, String file,String destfile) throws Exception {
	  try {
		  	File sdCardDir=Environment.getExternalStorageDirectory();
			File destDir = new File(sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian");
			if (!destDir.exists()) {
			   destDir.mkdirs();
			  }
		   destfile=sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian/"+destfile;
		   FileInputStream fis = new FileInputStream(file);
		   FileOutputStream fout = new FileOutputStream(destfile);
	       byte[] bytIn=getBytes(file);
	    //AES加密
	       KeyGenerator kgen = KeyGenerator.getInstance("AES");
	       kgen.init(128, new SecureRandom(pwd.getBytes()));
	       SecretKey skey = kgen.generateKey();
	       byte[] raw = skey.getEncoded();
	       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	       Cipher cipher = Cipher.getInstance("AES");
	       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	            //写文件
	       byte[] bytOut = cipher.doFinal(bytIn);
	  
	       getFile(bytOut,destfile);
	       fout.close();
	       fis.close();
	  } catch (Exception e) {
	   throw new Exception(e.getMessage());
	  }
	 }
	 public static boolean allowfile(String filename){
		 if(filename.endsWith(".doc"))
			 return true;
		 else if(filename.endsWith(".docx"))
			 return true;
		 else if(filename.endsWith(".txt"))
			 return true;
		 else if(filename.endsWith(".xlsx"))
			 return true;
		 else if(filename.endsWith(".xls"))
			 return true;
		 else if(filename.endsWith(".pdf"))
			 return true;
		 else if(filename.endsWith(".jpg"))
			 return true;
		 else if(filename.endsWith(".png"))
			 return true;
		 else if(filename.endsWith(".gif"))
			 return true;
		 else
			 return false;
	 }
	//解密文件
	 public static void dencryptfile(String pwd, String file,String destfile) throws Exception {
	  try {
		  	File sdCardDir=Environment.getExternalStorageDirectory();
			File destDir = new File(sdCardDir.getCanonicalPath()+"/little-girl");
			if (!destDir.exists()) {
			   destDir.mkdirs();
			  }
		   destfile=sdCardDir.getCanonicalPath()+"/little-girl/"+destfile;
		  // FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath()+"/littlegirl/"+file);
		   //FileOutputStream fout = new FileOutputStream(destfile);
	       byte[] bytIn=getBytes(sdCardDir.getCanonicalPath()+"/little-girl/jiamiwenjian/"+file);
	       
	       
	       KeyGenerator kgen = KeyGenerator.getInstance("AES");  
           kgen.init(128, new SecureRandom(pwd.getBytes()));  
           SecretKey secretKey = kgen.generateKey();  
           byte[] enCodeFormat = secretKey.getEncoded();  
           SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
           Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
           cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
            byte[] bytOut = cipher.doFinal(bytIn); 
	  
	   getFile(bytOut,destfile);
	//   fout.close();
	//   fis.close();
	  } catch (Exception e) {
	   throw new Exception(e.getMessage());
	  }
	 }
	 public static byte[] getBytes(String filePath){  
	        byte[] buffer = null;  
	        try {  
	            File file = new File(filePath);  
	            FileInputStream fis = new FileInputStream(file);  
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
	            byte[] b = new byte[1000];  
	            int n;  
	            while ((n = fis.read(b)) != -1) {  
	                bos.write(b, 0, n);  
	            }  
	            fis.close();  
	            bos.close();  
	            buffer = bos.toByteArray();  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return buffer;  
	    }  
	  
	    /** 
	     * 根据byte数组，生成文件 
	     */  
	    public static void getFile(byte[] bfile,String fileName) {  
	        BufferedOutputStream bos = null;  
	        FileOutputStream fos = null;  
	        File file = null;  
	        try {  
	            
	            file = new File(fileName);  
	            fos = new FileOutputStream(file);  
	            bos = new BufferedOutputStream(fos);  
	            bos.write(bfile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (bos != null) {  
	                
	                    try {
							bos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	                
	            }  
	            if (fos != null) {  
	                 
	                    try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	             
	            }  
	        }  
	    }  
}

		