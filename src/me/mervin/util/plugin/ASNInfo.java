package me.mervin.util.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.mervin.util.D;

/**
 *
 *   ASNInfo.java
 *   获取AS号信息  
 *  @author Mervin.Wong  DateTime 2013-9-25 下午2:46:16    
 *  @version 0.4.0
 */
public class ASNInfo {

	
	public ASNInfo(){
		
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String host = "202.118.7.129";
		String port = "59527";
		setProxy(host, port);
		
		String domain = "https://www.ultratools.com/tools/asnInfoResult?domainName=197859";
		//String domain = "http://www.baidu.com";
		String urlString = domain;
		try {
			URL url = new URL(urlString);
			D.p(url.getContent());
			
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");
			BufferedReader bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			String buf = contentBuf.toString();
			//String buf = contentBuf.toString();  
	       int beginIx = buf.indexOf("Country");  
	       //int endIx = buf.indexOf("上面四项依次显示的是");  
	       int endIx = beginIx+100;  
	       String result = buf.substring(beginIx, endIx);
			D.p(result);
			
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

    public static void setProxy(String host, String port) {  
        System.setProperty("proxySet", "true");  
        System.setProperty("proxyHost", host);  
        System.setProperty("proxyPort", port);  
    }  
}
