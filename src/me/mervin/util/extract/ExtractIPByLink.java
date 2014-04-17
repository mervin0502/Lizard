package me.mervin.util.extract;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mervin.core.Global.extractIP;

/**
 * ExtractIpLink.java
 * 提取一个链路上的逐跳IP地址
 *@author 王进法<Mervin.Wong>
 *@version 0.1.0
 *@date 2013-1-11下午3:26:17
 */
/****************************************************************************************
 * 
 * 使用步骤：
 * 1，对于从CAIDA上下载的数据包，解压后会得到一个XXX.warts文件，
 * 2，使用sc_wartscat 将多个文件合并，命令：sc_wartscat 文件名1 文件名2 -o 输出文件的文件名。此步骤可选
 * 3，sc_analysis_dump XXX.wrats > out.txt 将上一步生成的文件转换成文本格式并提取出想要的内容.sc_analysis_dump有多个参数可选择
 * 4，使用本程序开提取所需要的信息。例如，	ExtractIpLink ipLink = new ExtractIpLink("./data/ip.txt","./data/ips.txt",ExtractIpLink.extractContent.LINK_ALL);ipLink.run();
 ***************************************************************************************/
public class ExtractIPByLink {
	private String fileName = null; //文件名
	private String dstPath = null;//保存的文件名
	//提起内容内容的格式
	private extractIP extractContentValue = null;
	ArrayList<Long> ids = null;
	/**
	 *  初始化
	 * @param  fileName
	 * @param  dstPath 
	 * 
	 */
	public ExtractIPByLink(String fileName, String dstPath){
		this.fileName = fileName;
		this.dstPath = dstPath;
		//this.extractContentValue = extractContent.SOURCE_DESTINATION;
		this.extractContentValue = extractIP.LINK_ALL;
		this.ids  = new ArrayList<Long>();
	}
	/**
	 *  初始化
	 * @param  fileName
	 * @param  dstPath 
	 * @param  extractContentValue
	 */
	public ExtractIPByLink(String fileName, String dstPath, extractIP extractContentValue){
		this.fileName = fileName;
		this.dstPath = dstPath;
		this.extractContentValue = extractContentValue;
		this.ids  = new ArrayList<Long>();
	} 
	
	public void run() {
		
		try {
				BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
				BufferedWriter writer = new BufferedWriter(new FileWriter(this.dstPath));
				String line = null;
				Pattern p = null;
				long sourceIp, destinationIp, nextIp = 0;
				String str = new String(); 
				String temp = new String();
				int flag = 0;
				while((line = reader.readLine())!= null){
					line = line.trim();
					if(line.startsWith("T")){
						//System.out.println(line.trim());
						p = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
						Matcher m = p.matcher(line);
						switch(this.extractContentValue){
						//获取路由每跳，且最后一条不可达
						case LINK_I:
							if(line.contains("I")){
								m.find();
								sourceIp = this.ipToInt(m.group());
								m.find();
								destinationIp = this.ipToInt(m.group());
								str += sourceIp+"\t";
								while(m.find()){
									nextIp = this.ipToInt(m.group());
									str += nextIp+"\r\n"+nextIp+"\t"; 
								}
								str += destinationIp+"\r\n";
								//System.out.println(str);
								if(str.length() > 1024){
									writer.append(str);
									//writer.newLine();
									str = "";
								}
							}else{
								continue;
							}
							break;
						//获取路由每跳，且到达目的IP
						case LINK_C:
							if(line.contains("C")){
								m.find();
								sourceIp = this.ipToInt(m.group());
								m.find();
								destinationIp =  this.ipToInt(m.group());
								str += sourceIp+"\t";
								while(m.find()){
									nextIp = this.ipToInt(m.group());
									str += nextIp+"\r\n"+nextIp+"\t"; 
								}
								str += destinationIp+"\r\n";
								//System.out.println(str);
								if(str.length() > 1024){
									writer.append(str);
									//writer.newLine();
									str = "";
								}
							}else{
								continue;
							}
							break;
						//获取路由每跳
						case LINK_ALL:
							m.find();
							sourceIp = this.ipToInt(m.group());
							m.find();
							destinationIp = this.ipToInt(m.group());
							temp = sourceIp+"\t";
								while(m.find()){
									flag++;
									nextIp = this.ipToInt(m.group());
									temp += nextIp+"\r\n"+nextIp+"\t"; 
								}
								
								temp += destinationIp+"\r\n";
								if(line.contains("N")){
									if(flag == 0){
										temp = temp.substring(0, temp.indexOf(sourceIp+"\t"+destinationIp));
									}else{
										temp = temp.substring(0, temp.indexOf(nextIp+"\t"+destinationIp));
									}

								}
								System.out.println(line);
								writer.append(temp);
							//}
/*							if(str.length() > 1024){
								writer.append(str);
								//writer.newLine();
								str = "";
							}*/
							flag = 0;
							break;
						//只获取源IP和目的IP
						case SOURCE_DESTINATION:
						default :
							m.find();
							sourceIp = this.ipToInt(m.group());
							m.find();
							destinationIp = this.ipToInt(m.group());
							str += sourceIp+"\t"+destinationIp+"\r\n";
							if(str.length() > 1024){
								str += destinationIp+"\r\n";
								//System.out.println(str);
								writer.append(str);
								//writer.newLine();
								str = "";
							}
							
							break;
						}
					}
				}
				//writer.append(str);
				reader.close();
				writer.flush();
				writer.close();
			//}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(fileName+"文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Long ipToInt(String ipStr){
		//Debug.p(ipStr);
		String[] ipArr = ipStr.split("\\.");
		return (Long.parseLong(ipArr[0].toString())<<24)+(Long.parseLong(ipArr[1].toString())<<16)+(Long.parseLong(ipArr[2].toString())<<8)+(Long.parseLong(ipArr[3].toString()));
	}	
}
