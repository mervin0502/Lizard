/*****************************************************************************
 * 
 * Copyright [2013] [Mervin.Wong]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 *       
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 *****************************************************************************/
package me.mervin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.NumberType;

/**
 * FileTool.java
 * 文件操作类
 *@author 王进法<Mervin.Wong>
 *@version 0.4
 *
 **************************************
 * ChangLog
 * ==2013/10/18
 * # public boolean mkdir(String file)
 */

public class FileTool {
	private String srcFile = null;//读文件路径
	private String fileDirPath = null;
	private String dstFile = null;//写文件路径
	
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	
	/**
	 * 数字的类型
	 */
	public NumberType numberType = NumberType.INTEGER;
	
	public FileTool(){
		
	}
	public FileTool(String srcFile){
		File file = new File(srcFile);
		if(file.isFile()){
			this.srcFile  = srcFile;
		}else{
			this.fileDirPath = srcFile;
		}
		
	}	
	public FileTool(String srcFile, String dstFile){
		File file = new File(srcFile);
		if(file.isFile()){
			this.srcFile  = srcFile;
		}else{
			this.fileDirPath = srcFile;
		}
		
	}
	
	private void initFileRead(){
		if(this.srcFile != null){
			try {
				this.reader =  new BufferedReader(new FileReader(this.srcFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void initFileWrite(){
		if(this.srcFile != null){
			try {
				this.writer = new BufferedWriter(new FileWriter(this.dstFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/***********************************************************************************************
	 * 
	 * 
	 ***********************************************************************************************/
	/**
	 *  
	 *   统计文件中的某个字符串出现的次数
	 * @param str
	 * @return int
	 */
	public int statStringInFile(String str) {
		String line = new String();
		int num = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			try {
				while((line = reader.readLine()) != null){
					if(line.contains(str)){
						num++;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 *  
	 *  统计文件中的行数
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public int statFileLines(){
		int lineNum = 0;
		BufferedReader reader;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(this.srcFile));
			while((line = reader.readLine()) != null){
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					++lineNum;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lineNum;
	}
	
	/**
	 *  statNodeNum
	 *  统计网络拓扑文件中节点的数量
	 * @param fileName
	 * @param numberType
	 * @return int
	 */
	public int nodeNum(String fileName, NumberType numberType){
		HashSet<Number> nodesSet =  new HashSet<Number>();
		String line = null;
		String[] lineArr = null;
		try {
			this.reader = new BufferedReader(new FileReader(fileName));
			while((line = this.reader.readLine()) != null){
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					lineArr = line.split("\t|(\\s{1,})");
					for (int i = 0; i < lineArr.length; i++) {
						nodesSet.add(MathTool.str2Number(numberType, lineArr[i]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodesSet.size();
	}
	
	/**
	 * 
	 *  Function: 清空文件中的内容
	 * 
	 *  @param dstFile
	 */
	public void clear(String dstFile){
		this.dstFile = dstFile;
		File f = new File(dstFile);
		if(f.isFile()){
			f.delete();
		}
		if(f.isDirectory()){
			for(File f1:f.listFiles()){
				f1.delete();
			}
		}
		//f.createNewFile();
	}
	
	public boolean isExist(String file){
		return new File(file).exists();
	}
	/********************************************************************************************************
	 * 
	 * 写文件方法重载
	 * 
	 ********************************************************************************************************/
	/**
	 * 
	 *  将map里的键值对作为一行写入文件
	 * @param map Map数据
	 * @param dstFile 文件保存路径
	 * @param append 是否追加
	 */
	public void write(Map<Number, Number> map, String dstFile, boolean append){
		this.dstFile = dstFile;
		Set<Number> set = map.keySet();
		StringBuffer buffer= new StringBuffer();
		Object object = null;
		for (Iterator<Number> iterator = set.iterator(); iterator.hasNext();) {
			object = iterator.next();
			buffer.append(object.toString()).append("\t").append(map.get(object).toString()).append("\r\n");
		}
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			this.writer = new BufferedWriter(new FileWriter(this.dstFile, append));
			this.writer.write(buffer.toString());
			this.writer.flush();
			this.writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 *  将PairList数据写入文件
	 * @param list PairList数据
	 * @param dstFile 文件保存路径
	 * @param append 是否追加
	 */
	public void write(PairList<Number, Number> list, String dstFile, boolean append){
		this.dstFile = dstFile;
		StringBuffer buffer= new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			buffer.append(list.getL(i)).append("\t").append(list.getR(i)).append("\r\n");
		}
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			this.writer = new BufferedWriter(new FileWriter(this.dstFile, append));
			this.writer.write(buffer.toString());
			this.writer.flush();
			this.writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 
	 *  将集合写入文件
	 * @param c 集合
	 * @param dstFile 文件保存路径
	 * @param append 是否追加
	 */
	public void write(Collection<Number> c, String dstFile, boolean append){
		new File(dstFile).getParentFile().mkdirs();
		this.dstFile = dstFile;
		
		StringBuffer buffer= new StringBuffer();
		Number object = null;
		for (Iterator<Number> iterator = c.iterator(); iterator.hasNext();) {
			object = iterator.next();
			buffer.append(object.toString()).append("\r\n");
		}
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			this.writer = new BufferedWriter(new FileWriter(this.dstFile, append));
			this.writer.write(buffer.toString());
			this.writer.flush();
			this.writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 
	 *  将字符串写入文件
	 * @param str String
	 * @param dstFile 文件保存路径
	 * @param append 是否追加
	 */
	public void write(String str, String dstFile, boolean append){
		this.dstFile = dstFile;
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			this.writer = new BufferedWriter(new FileWriter(this.dstFile, append));
			this.writer.write(str);
			this.writer.flush();
			this.writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 *  将StringBuffer对象写入文件
	 * @param sb StringBuffer 数据
	 * @param dstFile 文件保存路径
	 * @param append 是否追加
	 */
	public void write(StringBuffer sb, String dstFile, boolean append) {
		// TODO Auto-generated method stub
		this.dstFile = dstFile;
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			this.writer = new BufferedWriter(new FileWriter(this.dstFile, append));
			this.writer.write(sb.toString());
			this.writer.flush();
			this.writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	/**
	 * 
	 *  将Set<Pair<Number>>对象写入文件 
	 *  @param pSet 节点对的集合
	 *  @param dstFile 目标文件
	 *  @param append true:在文件末追加;false:覆盖文件
	 */
	public void write(Set<Pair<Number>> pSet, String dstFile, boolean append){
		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p:pSet){
			sb.append(p.getL()).append("\t").append(p.getR()).append("\n");
		}
		this.write(sb, dstFile, append);
	}
	/**
	 * 
	 *  将map里的键值对作为一行写入文件
	 * @param map Map数据
	 * @param dstFile 文件保存路径
	 */
	public void write(Map<Number, Number> map, String dstFile){
		this.write(map, dstFile, false);
	}
	/**
	 * 
	 *  将PairList数据写入文件
	 * @param list PairList数据
	 * @param dstFile 文件保存路径
	 */
	public void write(PairList<Number, Number> list, String dstFile){
		this.write(list, dstFile, false);
	}
	/**
	 * 
	 *  将集合写入文件
	 * @param c 集合
	 * @param dstFile 文件保存路径
	 */
	public void write(Collection<Number> c, String dstFile){
		this.write(c, dstFile, false);
	}
	/**
	 * 
	 *  将字符串写入文件
	 * @param str String
	 * @param dstFile 文件保存路径
	 */
	public void write(String str, String dstFile){
		this.write(str, dstFile, false);
	}
	
	/**
	 * 
	 *  将StringBuffer对象写入文件
	 * @param sb StringBuffer 数据
	 * @param dstFile 文件保存路径
	 */
	public void write(StringBuffer sb, String dstFile) {
		// TODO Auto-generated method stub
		this.write(sb, dstFile, false);
	}
	/**
	 * 
	 *  将Set<Pair<Number>>对象写入文件 
	 *  @param pSet 节点对的集合
	 *  @param dstFile 目标文件
	 *  @param append true:在文件末追加;false:覆盖文件
	 */
	public void write(Set<Pair<Number>> pSet, String dstFile){
		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p:pSet){
			sb.append(p.getL()).append("\t").append(p.getR()).append("\n");
		}
		this.write(sb, dstFile);
	}
	/********************************************************************************************************
	 * 
	 * 读文件方法重载
	 * 
	 ********************************************************************************************************/
	/**
	 *  
	 *  对文件数据到集合中
	 * @param srcFile 文件夹或者文件路径
	 * @return HashSet<Number>
	 */
	public Set<Number> read2Set(String srcFile){
		return this.read2Set(srcFile, NumberType.INTEGER);
	}
	/**
	 *  
	 *  对文件数据到集合中
	 * @param srcFile
	 * @param numType
	 * @return HashSet<Number>
	 */
	public Set<Number> read2Set(String srcFile,NumberType numType){
		this.srcFile = srcFile;
		HashSet<Number> set = new HashSet<Number>();
		//this.initFileRead();
		try {
			BufferedReader reader =  new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			while ((line = reader.readLine()) != null) {
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					lineArr = line.split("\t|(\\s+)");
					for (int i = 0; i < lineArr.length; i++) {
//						D.p(lineArr[i]);
						set.add(MathTool.str2Number(numType, lineArr[i]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
	/**
	 *  
	 *  对文件数据到集合中
	 * @param srcFile
	 * @param numType
	 * @param col 需要提取的列,从1开始计数
	 * @return Set<Number>
	 */
	public Set<Number> read2Set(String srcFile,NumberType numType, int[] col){
//		public HashSet<Number> read2Set(String srcFile,NumberType numType, List<Integer> cols){
		this.srcFile = srcFile;
		HashSet<Number> set = new HashSet<Number>();

		try {
			BufferedReader reader =  new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			while ((line = reader.readLine()) != null) {
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					lineArr = line.split("\t|(\\s+)");
					for(int i = 0; i < col.length; i++){
						//D.p(lineArr[col[i]-1]);
						set.add(MathTool.str2Number(numType, lineArr[col[i]-1]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
	
	/**
	 * 
	 *  Function: 读取文件的内容到List类型中。
	 * 
	 *  @param srcFile
	 *  @param numType
	 *  @param col
	 *  @return  List<Number>
	 */
	@SuppressWarnings("resource")
	public List<Number> read2List(String srcFile, NumberType numType, int[] col){
		ArrayList<Number> list = new ArrayList<Number>();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			while ((line = reader.readLine()) != null) {
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					lineArr = line.split("\t|(\\s+)");
					for(int i = 0; i < col.length; i++){
						//D.p(lineArr[col[i]-1]);
						list.add(MathTool.str2Number(numType, lineArr[col[i]-1]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 *  
	 *  读取文件的内容到map中
	 * @param srcFile 文件夹或者文件路径
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile){
		 HashMap<Number, Number> map = new  HashMap<Number, Number>();
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						map.put(MathTool.str2Number(NumberType.INTEGER, lineArr[0]), MathTool.str2Number(NumberType.INTEGER, lineArr[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return map;
	}	
	/**
	 *  
	 *  读取文件的内容到map中
	 * @param srcFile
	 * @param numberType
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile, NumberType numberType){
		 HashMap<Number, Number> map = new  HashMap<Number, Number>();
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						map.put(MathTool.str2Number(numberType, lineArr[0]), MathTool.str2Number(numberType, lineArr[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return map;
	}
	/**
	 *  
	 *  读取文件的内容到map中
	 * @param srcFile
	 * @param leftNumberType
	 * @param rightNumberType
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile, NumberType leftNumberType, NumberType rightNumberType){
		 HashMap<Number, Number> map = new  HashMap<Number, Number>();
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						map.put(MathTool.str2Number(leftNumberType, lineArr[0]), MathTool.str2Number(rightNumberType, lineArr[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return map;
	}
	/**
	 * 将文本中的k和v列读取到map中
	 *  
	 *  @param srcFile
	 *  @param k 将k列作为map的key
	 *  @param v 将v列作为map的alue
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile, int k, int v){
		return this.read2Map(srcFile, k, NumberType.INTEGER, v, NumberType.INTEGER);
	}
	/**
	 * 将文本中的k和v列读取到map中
	 *  
	 *  @param srcFile
	 *  @param k 将k列作为map的key
	 *  @param leftNumberType k的类型
	 *  @param v 将v列作为map的alue
	 *  @param rightNumberType v的类型
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile, int k, NumberType leftNumberType, int v, NumberType rightNumberType){
		 Map<Number, Number> map = new  HashMap<Number, Number>();
		 File[] fileArr = this.fileArr(srcFile);
		 
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null && line.trim().length() > 0){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						//D.p(line);
						//D.p(line.length());
						map.put(MathTool.str2Number(leftNumberType, lineArr[k-1]), MathTool.str2Number(rightNumberType, lineArr[v-1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return map;
	}
	/**
	 * 将文本中的k和v列读取到map中
	 *  
	 *  @param srcFile 文件路径或者目录
	 *  @param k 将k列作为map的key
	 *  @param v 将v列作为map的alue
	 *  @param numberType 节点的类型
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> read2Map(String srcFile, int k, int v, NumberType numberType){
		 Map<Number, Number> map = new  HashMap<Number, Number>();
		 File[] fileArr = this.fileArr(srcFile);
		 
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						//D.p(line);
						map.put(MathTool.str2Number(numberType, lineArr[k-1]), MathTool.str2Number(numberType, lineArr[v-1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return map;
	}
	/**
	 *  
	 *  读取文件的内容到pair中
	 * @param srcFile
	 * @param numberType
	 * @return Set<Pair>
	 */
	public Set<Pair<Number>> read2SetPair(String srcFile, NumberType numberType){
		// HashMap<Number, Number> map = new  HashMap<Number, Number>();
		 HashSet<Pair<Number>> pair = new HashSet<Pair<Number>>();
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						pair.add(new Pair<Number>(MathTool.str2Number(numberType, lineArr[0]), MathTool.str2Number(numberType, lineArr[1])));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return pair;
	}
	
	/**
	 *  
	 *  读取文件的内容到List<Pair<Number>>中
	 * @param srcFile
	 * @param numberType
	 * @return HashSet<Pair>
	 */
	public List<Pair<Number>> read2ListPair(String srcFile, NumberType numberType){
		// HashMap<Number, Number> map = new  HashMap<Number, Number>();
		 List<Pair<Number>> pair = new ArrayList<Pair<Number>>();
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						pair.add(new Pair<Number>(MathTool.str2Number(numberType, lineArr[0]), MathTool.str2Number(numberType, lineArr[1])));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 return pair;
	}
	
	/**
	 * 
	 *  
	 *  @param srcFile
	 *  @param numberType
	 *  @return
	 */
	public PairList<Number, Number> read2PairList(String srcFile, NumberType numberType){
		PairList<Number, Number> pairList = new PairList<Number, Number>();
//		D.p(srcFile);
		 File[] fileArr = this.fileArr(srcFile);
		 for (int i = 0; i < fileArr.length; i++) {
			try {
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(fileArr[i]));
				String line = null;
				String[] lineArr = null;
				while((line = reader.readLine()) != null){
					if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
						lineArr = line.split("\t|(\\s+)");
						pairList.add(MathTool.str2Number(numberType, lineArr[0]), MathTool.str2Number(numberType, lineArr[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for
		 
		 return pairList;
	}
	/*
	 * **************************************************************************************
	 * 
	 * 
	 * 
	 *
	***************************************************************************************/
	/**
	 *  
	 *  获取文件对象数组
	 * @param path
	 * @return File[]
	 */
	public File[] fileArr(String path){
		File[] fileArr = null;
		//D.p(path);
		File f = new File(path);
		//D.p(f.getAbsoluteFile());
		if(f.isDirectory()){
			fileArr = f.listFiles();
		}else{
			fileArr = new File[1];
			fileArr[0] = f;
		}
		return fileArr;
	}
	/**
	 *  
	 *  获取文件对象数组
	 * @param path
	 * @return File[]
	 */
	public File[] fileArr(String dir, Collection<String> collection){
		File[] fileArr = null;
		//D.p(path);
		File f = new File(dir);
		//D.p(f.getAbsoluteFile());
		if(f.isDirectory()){
			fileArr = f.listFiles(new FileSelector(collection));
		}else{
			fileArr = new File[1];
			fileArr[0] = f;
		}
		return fileArr;
	}
	
	/**
	 *  
	 *  获取文件对象数组
	 * @param path
	 * @return File[]
	 */
	public File[] fileArr(String dir, String filter){
		File[] fileArr = null;
		//D.p(path);
		File f = new File(dir);
		//D.p(f.getAbsoluteFile());
		if(f.isDirectory()){
			fileArr = f.listFiles(new FileSelector(filter));
		}else{
			fileArr = new File[1];
			fileArr[0] = f;
		}
		return fileArr;
	}
	/**
	 *  
	 *  创建路径目录
	 * @param file 文件或者目录路径
	 * @return boolean true:file为目录;false:file为文件
	 */
	public boolean mkdir(String file){
		File f = new File(file);
		if(f.isFile()){
			return f.getParentFile().mkdirs();
		}else{
			return f.mkdirs();
		}
	}
	/**
	 *  
	 *  合并文件 文件格式是单列，或者两列
	 * @param file1
	 * @param file2
	 * @param dstFile
	 * @param numberType  节点类型
	 * @param cols 单列还是多列
	 */
	public void combine(String file1, String file2, String dstFile, NumberType numberType, int cols){
		File[] fileArr = new File[2];
		fileArr[0] = new File(file1);
		fileArr[1] = new File(file2);
		this._combine(fileArr, dstFile, numberType, cols);
	}
	/**
	 * 
	 *  合并文件 文件格式是单列，或者两列
	 * @param fileArr
	 * @param dstFile
	 * @param numberType  节点类型
	 * @param cols 单列还是多列
	 */
	public void combine(File[] fileArr, String dstFile, NumberType numberType, int cols){
		this._combine(fileArr, dstFile, numberType, cols);
	}
	/**
	 *  
	 *  合并文件夹里的文件, 文件格式是单列，或者两列  对网路是又向，还是无向不关注
	 * @param srcDir  目录 
	 * @param dstFile 目标文件名
	 * @param numberType  节点类型
	 * @param cols 单列还是多列
	 *
	 */
	public void combine(String srcDir, String dstFile, NumberType numberType, int cols){
		File[] fileArr = this.fileArr(srcDir);
		this._combine(fileArr, dstFile, numberType, cols);
	}
	
	/**
	 *  
	 *  合并文件夹里的网络文件, 
	 *  注： 对网络是又向，还是无向不关注
	 * @param srcDir  目录 
	 * @param dstFile 目标文件名
	 * @param numberType  节点类型
	 *
	 */
	public void combine(String srcDir, String dstFile, NumberType numberType){
		File[] fileArr = this.fileArr(srcDir);
		this._combine(fileArr, dstFile, numberType, 2);
	}
	/*
	 *  _combine
	 *  合并文件夹里的文件, 文件格式是单列，或者两列
	 * @param fileArr
	 * @param dstFile
	 * @param numberType
	 * @param cols
	 */
	private void _combine(File[] fileArr, String dstFile, NumberType numberType, int cols){
		new File(dstFile).getParentFile().mkdirs();//创建目录文件夹
		
		if(cols == 1){
			HashSet<Number> nodeSet= new HashSet<Number>();
			String line = null;
			for (int i = 0; i < fileArr.length; i++) {
				try {
					reader = new BufferedReader(new FileReader(fileArr[i]));
					while((line=reader.readLine()) != null){
						if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
							nodeSet.add(MathTool.str2Number(numberType, line.toString()));
							//D.p(line);
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//写文件
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(dstFile));
					Iterator<Number> it = nodeSet.iterator();
					StringBuffer sb = new StringBuffer();
					while(it.hasNext()){
						sb.append(it.next().toString()).append("\r\n");
						if(sb.length() > 1024*1024*20){
							writer.append(sb.toString());
							sb.delete(0, sb.length());
						}
					}
					writer.append(sb.toString());
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			BufferedReader reader = null;
			PairSet<Number> edgeSet = new PairSet<Number>();
			String line = null;
			String[] lineArr = null;
			Number pre, post;
			for (int i = 0; i < fileArr.length; i++) {
				try {
					//D.p(fileArr[i].getAbsolutePath());
					reader = new BufferedReader(new FileReader(fileArr[i]));
					
					while((line = reader.readLine()) != null){
						if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
							lineArr = line.split("\t|(\\s{1,})");
							//D.p(line);
							pre = MathTool.str2Number(numberType, lineArr[0]);
							post = MathTool.str2Number(numberType, lineArr[1]);
							//D.p(lineArr[0]+"###"+lineArr[1]);
	
							if(!pre.equals(post)){
								edgeSet.add(new Pair<Number>(pre, post));
							}
						}
					}
					reader.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//for
			//D.p(edgeSet.size());
			BufferedWriter writer = null;
			Pair<Number> temp = null;
			try {
				writer = new BufferedWriter(new FileWriter(dstFile));
				Iterator<Pair<Number>> it = edgeSet.iterator();
				StringBuffer sb = new StringBuffer();
				while(it.hasNext()){
					temp = it.next();
					sb.append(temp.getL()).append("\t").append(temp.getR()).append("\r\n");
					/*if(sb.length() > 1024*1024*20){
						writer.append(sb.toString());
						sb.delete(0, sb.length());
					}*/
				}
				writer.append(sb.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
}


class FileSelector implements FilenameFilter{

	private String filter = null;
	private Collection<String> filterCollection = null;
	public FileSelector(String filter){
		this.filter = filter;
	}
	public FileSelector(Collection<String> filterCollection){
		this.filterCollection = filterCollection;
	}
	 /* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File file, String str) {
		// TODO 自动生成的方法存根
		boolean flag = true;
		if(filter != null){
			if(!str.contains(filter)){
				flag = false;
			}
		}
		if(filterCollection != null){
			for(String filterStr:filterCollection){
				if(!str.contains(filterStr)){
					flag = false;
				}
			}
		}
		
		return flag;
	}
	
}