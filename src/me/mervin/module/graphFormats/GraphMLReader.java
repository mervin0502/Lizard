package me.mervin.module.graphFormats;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Node;
import me.mervin.core.Global.AttributeType;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;




 /**
 *   GraphMLReader.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-14 下午9:19:05    
 *  @version 0.4.0
 */
public class GraphMLReader {

	/*
	 * graphml文件
	 */
	private String srcFile = null;
	
	/*
	 * root node
	 */
	private Element root = null;
	
	/*
	 * attribute map
	 */
	private Map<String, String> nodeAttrValueMap = null;
	private Map<String, AttributeType> nodeAttrTypeMap = null;
	
	
	private Map<String, String> edgeAttrValueMap = null;
	private Map<String, AttributeType> edgeAttrTypeMap = null;
	
	private Map<String, String> netAttrValueMap = null;
	private Map<String, AttributeType> netAttrTypeMap = null;	
	/*
	 * network name and is directed netowrk
	 */
	private String netName = null;
	private NetType netType = null;
	
	
	
	public GraphMLReader(String file){
		this.srcFile = file;
	}
	
	public Document read(){
		SAXReader  reader =  new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(new File(this.srcFile));
		} catch (DocumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return doc;
	}
	
	 public Element getRootElement(Document doc){
	       return doc.getRootElement();
	 }
	 
	 
	 public void parser(){
		 root = this.getRootElement(read());
		  // 枚举所有子节点
		    for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
		       Element element = (Element) i.next();
		       // do something
		       //D.p(element.getQName());
		       //D.p("\r\n");
		       
		    }
		    // 枚举名称为foo的节点
		    String foo = "node";
		    //root.e
		    for ( Iterator i = root.elementIterator(foo); i.hasNext();) {
		       Element foo1 = (Element) i.next();
		       // do something
		       //D.p(foo1.getName());
		    }
		    // 枚举属性
		    for ( Iterator i = root.attributeIterator(); i.hasNext(); ) {
		       Attribute attribute = (Attribute) i.next();
		       // do something
		    }
	 }
	 
	 public Network createNet(){
		Network net = new Network();
		
		Number id = null, preId = null, postId = null;
		Node node = null;
		
		root = this.getRootElement(read());
		Element e1 = null, e2 = null, e3 = null;
		Attribute attr1 = null, attr2 = null;
		
		for ( Iterator<Element> i = root.elementIterator(); i.hasNext(); ) {
		   e1 = (Element) i.next();
		   /*
		    * network attribute map 
		    */
		   if(e1.getName().equalsIgnoreCase("key")){
			   this._extractAttr(e1);
		   }
		   
		   /*
		    * network graph remark
		    */
		   if(e1.getName().equalsIgnoreCase("graph")){
			   /*
			    * network direction
			    */
			   if(e1.attributeValue("edgedefault").equalsIgnoreCase("directed")){
				   
				   this.netType = NetType.DIRECTED;
			   }else{
				   this.netType = NetType.UNDIRECTED;
			   }
			   net.setNetType(this.netType);
			   /*
			    * iterator node and edge
			    */
			   for(Iterator<Element> it = e1.elementIterator(); it.hasNext();){
				   e2 = it.next();
				   
				   //node
				   if(e2.getName().equalsIgnoreCase("node")){
					   id = Integer.valueOf(e2.attributeValue("id").substring(1));
					   node = new Node(id);

					   for(Iterator<Element> it2 = e2.elementIterator(); it2.hasNext();){
						   e3 = it2.next();
						   if(e3.getName().equalsIgnoreCase("data")){
							   node.setAttr(this.nodeAttrValueMap.get(e3.attributeValue("key")), e3.getTextTrim());
						   }
					   }
					   
					   net.insertNode(node);
				   }
				  
				   
				   //edge
				   if(e2.getName().equalsIgnoreCase("edge")){
					   
					   preId = Integer.valueOf(e2.attributeValue("source").substring(1));
					   postId = Integer.valueOf(e2.attributeValue("target").substring(1));
					   
					   net.insertEdge(preId, postId);
					   
					   for(Iterator<Element> it3 = e2.elementIterator(); it3.hasNext();){
						   e3 = it3.next();
						   if(e3.getName().equalsIgnoreCase("data")){
							   //D.p(this.nodeAttrValueMap.get(e3.attributeValue("key")));
							   //D.p(e3.getTextTrim());
							   net.setEdgeAttr(preId, postId,this.edgeAttrValueMap.get(e3.attributeValue("key")), e3.getTextTrim());
						   }
					   }
				   }
			   }
			   
		   }
		}
		return net;
	 }
	 
	 /*
	  * 提取属性值映射
	  */
	 private void _extractAttr(Element e){
		 String idValue = e.attributeValue("id");
		 //D.p(idValue);
		 if(e.attribute("for") != null){
			 
			 if(e.attributeValue("for").equalsIgnoreCase("node")){
				 ;
				 if(this.nodeAttrValueMap == null){
					 this.nodeAttrValueMap = new HashMap<String, String>();
					 this.nodeAttrTypeMap = new HashMap<String, AttributeType>();
				 }
				 this.nodeAttrValueMap.put(idValue, e.attributeValue("attr.name"));
				 this.nodeAttrTypeMap.put(idValue, this._strToEnum(e.attributeValue("attr.type")));
			 }else if(e.attributeValue("for").equalsIgnoreCase("edge")){
				 if(this.edgeAttrValueMap == null){
					 this.edgeAttrValueMap = new HashMap<String, String>();
					 this.edgeAttrTypeMap = new HashMap<String, AttributeType>();
				 }
				 ;
				 this.edgeAttrValueMap.put(idValue, e.attributeValue("attr.name"));
				 this.edgeAttrTypeMap.put(idValue, this._strToEnum(e.attributeValue("attr.type")));				 
			 }
		 }else{
			 
		 }

	 }
	
	 /*
	  * 提取节点
	  */
	 private void _extractNode(Element e){
		 
	 }
	 
	 /*
	  * 提取边
	  */
	 private void _extractEdge(Element e){
		 
	 }
	 
	 /*
	 * 根据string判断是那种数据类型 
	 */
	 private AttributeType _strToEnum(String str){
		 if(str.equalsIgnoreCase("integer")){
			 return AttributeType.INTEGER;
		 } else if(str.equalsIgnoreCase("long")){
			 return AttributeType.LONG;
		 } else if(str.equalsIgnoreCase("float")){
			 return AttributeType.FLOAT;
		 } else if(str.equalsIgnoreCase("double")){
			 return AttributeType.DOUBLE;
		 } else if(str.equalsIgnoreCase("string")){
			 return AttributeType.STRING;
		 } else{
			 return AttributeType.STRING;
		 }
	 }
	
	 
	 /**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		//GraphMLReader r = new GraphMLReader("")
		
		GraphMLReader r = new GraphMLReader("../data/ba-r.graphml");
		r.createNet();
	}
	

}
class AttrVisitor extends VisitorSupport {
    public void visit(Element element){
        System.out.println(element.getName());
    }
    public void visit(Attribute attr){
        System.out.println(attr.getName());
    }
 }