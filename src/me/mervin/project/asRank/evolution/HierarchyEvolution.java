package me.mervin.project.asRank.evolution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.tree.Link;
import me.mervin.core.tree.Tree;
import me.mervin.core.tree.Vertex;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.PairList;


 /**
 *   HierarchyEvolution.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月21日 下午4:47:15    
 *  @version 0.4.0
 */
public class HierarchyEvolution {
	
	private String srcDir = null;
	private String dstDir = null;
	
	
	private List<Integer> cliqueList = null;
	//private PairList<Number, Number> pairList = new PairList<Number, Number>();
	private Map<Number, LinkedList<Number>> pairList = null;
	
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
////		String srcFile = "data/t.txt";

		
//		ExecutorService pool = Executors.newFixedThreadPool(4);
		ExecutorService pool = Executors.newSingleThreadExecutor();
		Thread he = null;
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-tree\\";
		String date = null;
		for(int y = 2013; y <= 2013; y++){
			for(int m = 5; m <= 6; m++){
				//y = 2000;
				//m = 5;
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+".as-rel.txt";
				if(new File(srcFile).exists()){
					he = new HierarchyCalculate(srcFile, dstDir, date);
					pool.execute(he);
				}else{
					Log.a(srcFile+"\r\n", dstDir+"file_noexist.txt");
				}
				//break;
			}
			
		}//for
		pool.shutdown();
		
		
	}

}


class HierarchyCalculate extends Thread {
	private List<Integer> cliqueList = null;
	//private PairList<Number, Number> pairList = new PairList<Number, Number>();
	private Map<Number, LinkedList<Number>> pairList = null;
	
	private String srcFile = null;
	private String dstDir = null;
	private String dstFile = null;
	private String date = null;
	/**
	 */
	public HierarchyCalculate() {
		// TODO 自动生成的构造函数存根
	}
	public  HierarchyCalculate(String srcFile, String dstDir, String date){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
		this.date = date;
	}
	public void run(){
		this._read2Pair(srcFile);
//		D.p(this.pairList);
//		System.exit(0);
		D.p("read "+srcFile+" done");
		
		Tree t = null;
		Queue<Number> queue = new LinkedList<Number>();
		Number id = null;
		LinkedList<Number> list = null;
		String dstFile = null;
		D.p(cliqueList);
		Integer root = null;
		for(Iterator<Integer> it = cliqueList.iterator(); it.hasNext();){
		//for(Integer root:cliqueList){
			root = it.next();
 			t = new Tree();
			t.setRoot(t.addVertex(root));
			queue.add(root);
			while(!queue.isEmpty()){
				id = queue.poll();
				list = this.pairList.get(id);
				
				//D.p("@"+id+" "+list);
				if(list != null){
					for(Number childId:list){
						if(!queue.contains(childId)){
							queue.add(childId);
						}
						t.insertLink(id, childId);
					}				
				}

			}//while
			D.p("Tree: "+root+" construct done");
			
			/*
			 * compute the quantity related of Tree
			 * 以每个节点为根节点的树
			 * 1.树的直接子节点数量：
			 * 2.树的所有子节点数量
			 * 3.树的高度
			 * 4.该树根节点所在的层
			 */
			FileTool ft = new FileTool();
			
			dstFile = dstDir+date+"\\"+root+"-tree.txt";
			//ft.write(t.BFS(), dstFile);
			t.BFS(dstFile);
			D.p("Write Tree:"+date+"=>"+root);
			TreeTool tt = new TreeTool(t); 
			Queue<Number> path = new LinkedList<Number>();
			tt.script(t.root.id, 0, path);
			tt.script3();
			dstFile = dstDir+date+"\\"+root+"-size.txt";
			ft.write(tt.sizeMap, dstFile);
			dstFile = dstDir+root+"-level.txt";
			ft.write(tt.levelMap, dstFile);
			dstFile = dstDir+root+"-parent.txt";
			ft.write(tt.parentMap, dstFile);
			dstFile = dstDir+root+"-children.txt";
			ft.write(tt.childrenMap, dstFile);
			dstFile = dstDir+root+"-leaf.txt";
			ft.write(tt.leafSet, dstFile);
			dstFile = dstDir+"h.txt";
			ft.write(root+"\t"+tt.h+"\r\n", dstFile, true);
			D.p("Tree: "+root+" statistics done");
		}
		D.p("###############################");		
	}
	
	private void _read2Pair(String srcFile){
		String line = null;
		String[] lineArr = null;
		cliqueList = new ArrayList<Integer>();
		pairList = new HashMap<Number, LinkedList<Number>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine())!= null){
				if(line.charAt(0) == '#'){
					if(line.contains("clique:")){
						line = line.substring(line.indexOf(":")+1).trim();
						lineArr = line.split("\\s+");
						for(String str:lineArr){
							this.cliqueList.add(Integer.parseInt(str));
						}
					}
				}else{
					lineArr = line.split("\\|");
					if(lineArr[2].equals("-1")){
						int l = Integer.parseInt(lineArr[0]);
						int r = Integer.parseInt(lineArr[1]);
						
						if(this.pairList.containsKey(l)){
							this.pairList.get(l).add(r);
						}else{
							LinkedList<Number> list = new LinkedList<Number>();
							list.add(r);
							this.pairList.put(l, list);
						}  
					}

				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}


class TreeTool{
	
	private Tree tree = null;
	
	private Number rootId = null;
	/*
	 * 以当前节点为根节点构建一个子树
	 */
	public Map<Number, Number> sizeMap = new HashMap<Number, Number>();
	public Map<Number, Number> levelMap = new HashMap<Number, Number>();
	public Map<Number, Number> parentMap = new HashMap<Number, Number>();
	public Map<Number, Number> childrenMap = new HashMap<Number, Number>();
	public Set<Number> leafSet = new HashSet<Number>(); 
	
	public int h = 0;//树的高度
	
	TreeTool(Tree t){
		this.tree = t;
		this.rootId = t.root.id;
		//this.tempId = this._init(this.rootId);
	}
	
	
	public int script(Number id, int level, Queue<Number> path){
		/*
		 * 访问最左子树的节点
		 */
		Number tempId = id;
		Number parentId = null;
		LinkedList<Link> linkList = null;
		//D.p(id+"##"+level);
//		while(true){
			//D.p(tempId+":"+this.tree.getChildrenId(tempId));
			this.parentMap.put(tempId, this.tree.getVertexById(tempId).getParent().size());
			this.levelMap.put(tempId, level);
			if(this.tree.isHasChild(tempId)){
				/*
				 * 非叶子节点的子节点数量
				 */
				parentId = tempId;
				this.childrenMap.put(tempId, this.tree.getVertexById(tempId).getChildren().size());
				/*
				 * 有孩子节点，level+1
				 */
				level++;
				if(this.h < level){ 
					this.h = level;
				}
				int num = 0;
				linkList = this.tree.getVertexById(tempId).getChildren();
				for(Link l:linkList){
					tempId = l.getChildId();
					//D.p("###"+tempId);
					num += this.script(tempId, level, path);
					
				}
				this.sizeMap.put(parentId, num);
				return num+1;				
			}else{
				this.childrenMap.put(tempId, 0);
				this.sizeMap.put(tempId, 0);
				return 1;
			}
//		}
	}	
	
	/**
	 * 有递归式改为
	 */
	public void script2(){
		int level = 0;
		int high = 0;
		
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(tree.root.id);
		queue.add(-1);
		
		
		Set<Number> visited = new HashSet<Number>();
		visited.add(tree.root.id);
		
		Number curId = null;
		Number childId = null;
		LinkedList<Link> linkList = null;
		//层次遍历
		while(true){
//			D.p(queue);
			curId = queue.poll();
//			curId = queue.peek();
//			D.p(curId);
			if(!curId.equals(-1)){
				//父节点数量
				parentMap.put(curId, tree.getVertexById(curId).getParent().size());
				//节点所在的层
				levelMap.put(curId, level);
				//初始化sizeMap
				sizeMap.put(curId, 0);
				if(tree.isHasChild(curId)){
					//存在孩子节点
					linkList = tree.getVertexById(curId).getChildren();
					//孩子节点数量
					childrenMap.put(curId, linkList.size());
					//遍历孩子节点
					//D.p(linkList.size());
					for(Link l:linkList){
						childId = l.getChildId();
						if(!visited.contains(childId)){
							queue.add(childId);
							visited.add(childId);
						}
					}
				}else{
					//叶子节点
					childrenMap.put(curId, 0);
					leafSet.add(curId);
				}
			}else{
				//新的一层
				queue.add(-1);
				if(queue.peek().equals(-1)){
					break;
				}
				level++;
			}
		}//while
		
		/*
		 * 由叶子节点返回到根节点，同时计算size
		 */
//		Vertex v = null;
//		for(Number tempId:leafSet){
//			v = tree.getVertexById(tempId);
//			if(!v.getParent().isEmpty()){
//				
//			}
//		}

		this.h = level+1;
	}
	
	public int size(Number id){
		/*
		 * 访问最左子树的节点
		 */
		Number tempId = id;
		Number parentId = null;
		LinkedList<Link> linkList = null;
		if(this.tree.isHasChild(tempId)){
			/*
			 * 非叶子节点的子节点数量
			 */
			parentId = tempId;
			int num = 0;
			linkList = this.tree.getVertexById(tempId).getChildren();
			for(Link l:linkList){
				tempId = l.getChildId();
				num += this.size(tempId);
				
			}
			this.sizeMap.put(parentId, num);
			return num+1;				
		}else{
			return 1;
		}
	}		
	
	/*
	 * 先序
	 */
	public void script3(){
		
		
		
		Set<Number> vSet = tree.getAllVertexId();
		for(Number vId:vSet){
			tree.getVertexById(vId).setAttr("flag", 0+"");
			sizeMap.put(vId, 0);
		}
		
		Stack<Number> stack = new Stack<Number>();
		stack.add(tree.root.id);
		
		LinkedList<Number> pipe = new LinkedList<Number>();
		pipe.add(tree.root.id);
		tree.getVertexById(tree.root.id).setAttr("flag", 0+"");
		
		Number parentId = null, curId = null;
		Number childId = null;
		int index = 0;
		Vertex v = null;
		while(!pipe.isEmpty()){
			curId = pipe.getLast();
			if(!tree.isHasChild(curId)){
				sizeMap.put(parentId, sizeMap.get(parentId).intValue()+1);
				pipe.pollLast();
				continue;
			}
			v = tree.getVertexById(curId);
			
			index = Integer.parseInt(v.getAttr("flag"));
			if(index == v.getChildren().size()){
				index +=1;
				v.setAttr("flag", index+"");
				pipe.pollLast();
				if(!pipe.isEmpty()){
					parentId = pipe.getLast();
					sizeMap.put(parentId, sizeMap.get(parentId).intValue()+sizeMap.get(curId).intValue()+1);
					continue;
				}else{
					break;
				}
			}
			if(index == v.getChildren().size()+1){
				index = 0;
				sizeMap.put(curId, 0);
			}
			childId = v.getChildIdByIndex(index);
			index +=1;
			v.setAttr("flag", index+"");
			pipe.add(childId);
			parentId = curId;
		}
	}
}