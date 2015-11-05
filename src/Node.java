import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


abstract class Node{
	protected long created;
	protected long lastUpdated;
	protected long lastAccessed;
	protected String name;
	Directory parent;
	
	public Node(String name,Node parent) throws Exception{
		if(parent instanceof File){
			throw new Exception();
		}
		created=System.currentTimeMillis();
		lastUpdated=System.currentTimeMillis();
		lastAccessed=System.currentTimeMillis();
		this.name=name;
		this.parent=(Directory)parent;
	}
	
	public String getPath(){
		if(parent==null){
			return "/";
		}
		String parentPath=parent.getPath();
		return (parentPath.equals("/")?"":parentPath)+"/"+name;
	}
	
	public Node copyNode() throws Exception{
		return copyNode(this.name);
	}
	
	public abstract Node copyNode(String name) throws Exception;
	
	protected boolean delete(){
		if(parent==null){
			return false;
		}
		return parent.deleteChild(this);
	}
	
	protected boolean addChild(Node child){
		return false;
	}
	protected boolean containsChild(String childName){
		return false;
	}
	protected Node getChild(String childName){
		return null;
	}
	
	public String ls() throws Exception{
	    throw new Exception();	
	}
}

class File extends Node{
	String content;

	public File(String name,Node parent,String content) throws Exception{
         super(name,parent);
         this.content=content;
	}
	
	public Node copyNode(String name) throws Exception{
		return new File(name,null,content);
	}
}

class Directory extends Node{
	HashMap<String,Node> children;
	
	public Directory(String name,Node parent) throws Exception{
		super(name,parent);
		children=new HashMap<String,Node>();
	}
	
	public Node copyNode(String name) throws Exception{
		Directory dirCopy=new Directory(name,null);
		Set<Map.Entry<String,Node>> entrySet=children.entrySet();
		for(Map.Entry<String,Node> entry:entrySet){
			Node childCopy=entry.getValue().copyNode();
			dirCopy.addChild(childCopy);
		}
		return dirCopy;
	}
	
	public String ls(){
		Iterator<String> iter=children.keySet().iterator();
		StringBuilder result=new StringBuilder();
		while(iter.hasNext()){
			result.append(iter.next()+" ");
		}
		return result.length()>0?result.substring(0,result.length()-1):"";
	}
	
	protected boolean deleteChild(Node child){
		return children.remove(child.name)!=null;
	}
	
	protected boolean addChild(Node child){
		if(child!=null){
			child.parent=this;
			children.put(child.name,child);
			return true;
		}
		else{
			return false;
		}
	}
	
	protected boolean containsChild(String childName){
		return children.containsKey(childName);
	}
	
	protected Node getChild(String childName){
		return children.get(childName);
	}
}
