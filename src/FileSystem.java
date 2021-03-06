import java.util.*;
/**
 * @author xuanhaozhao
 * 
 */
public class FileSystem {
    Node root;
    Node curNode;
    
    
    public FileSystem() throws Exception{
    	root=new Directory("/",null);
    	curNode=root;
    }
    /**
     * This method is to print out the path of the current place
     */
    public void pwd(){
    	System.out.println(curNode.getPath());
    }
    
    /**
     * This method lists out everything under the current directory
     */
    public void ls() throws Exception{
    	System.out.println(curNode.ls());
    }
    
    /**
     * This method takes in a path which might contains "." and ".." and multiple slashes like "///" and turn it to
     * a path that only contains directories or files and slashes.
     * @param originalPath
     * @return 
     * @throws IllegalArgumentException
     * @throws Exception
     */
    protected String simplifyPath(String originalPath) throws IllegalArgumentException,Exception{
    	if(originalPath==null){
    		throw new IllegalArgumentException();
    	}
    	LinkedList<String> stack=new LinkedList<String>();
    	String[] parsedPath=originalPath.split("/");
    	StringBuilder result=new StringBuilder();
    	
    	if(originalPath.startsWith("/")){
			for(String s:parsedPath){
				if(!s.equals("")&&!s.equals(".")){
					if(s.equals("..")){
						if(!stack.isEmpty()){
							stack.pop();
						}
					}
					else{
					    stack.push(s);
					}
				}
			}
    	}
    	else{
    		for(String s:parsedPath){
    			if(!s.equals("")&&!s.equals(".")){
    				if(s.equals("..")&&!stack.isEmpty()&&!stack.peek().equals("..")){
    					stack.pop();
    				}
    				else{
    					stack.push(s);
    				}
    			}
    		}
    	}
    	
    	if(originalPath.startsWith("/")){
    		result.append("/");
    	}
    	while(stack.size()>0){
    		result.append(stack.removeLast()+"/");
    	}
    	if(!stack.isEmpty()){
    		result.append(stack.removeLast());
    	}
    	return result.toString();
    }
    
    /**
     * This method can find any node on the specified path(usually the last one or second to the last one), if the node is on the path.
     * Otherwise the method throws an exception.
     * an exception.
     * @param path
     * @param nodePos
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     * @throws Exception
     */
    protected Node findNodeOnPath(String[] path,int nodePos) throws IllegalArgumentException, NoSuchElementException,Exception{
    	if(path==null){
    		throw new IllegalArgumentException();
    	}
    	
    	Node cur;
    	if(path.length==0||path[0].equals("")){
    		cur=root;
    	}
    	else{
    		cur=curNode;
    	}
    	
    	for(int i=(path.length!=0&&!path[0].equals("")?0:1);i<=nodePos;i++){
    		if(path[i].equals("..")){
    			if(cur.parent!=null){
    				cur=cur.parent;
    			}
    		}
    		else if(cur.containsChild(path[i])){
        		cur=cur.getChild(path[i]);
        	}
        	else{
        		throw new NoSuchElementException();
        	}
    	}
    	return cur;
    }
    
    
    /**
     * This method is to navigate through the file system.
     * @param toDirectory
     */
    public void cd(String toDirectory) throws IllegalArgumentException,Exception{
    	if(toDirectory==null){
    		throw new IllegalArgumentException();
    	}
    	
    	String path=simplifyPath(toDirectory);
    	Node cur=null;
    	String[] parsedPath=path.split("/");
    	try{
    		cur=findNodeOnPath(parsedPath,parsedPath.length-1);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("No such directory");
    		return;
    	}

        if(cur instanceof Directory){
        	curNode=cur;
        	cur.setLastAccessed(System.currentTimeMillis());
        }
        else{
        	System.out.println("Not a directory");
        }
    }
    
    public void rm(String path) throws IllegalArgumentException,Exception{
    	if(path==null){
    	     throw new IllegalArgumentException();
    	}
    	
    	path=simplifyPath(path);
    	Node cur;
    	String[] parsedPath=path.split("/");
    	
    	try{
    		cur=findNodeOnPath(parsedPath,parsedPath.length-1);
    		cur.delete();
    	}
    	catch(NoSuchElementException n){
    		System.out.println("The specified path is invalid.");
        	return;
    	}
    }
    
    public void readfile(String path) throws IllegalArgumentException,Exception{
    	if(path==null){
    		throw new IllegalArgumentException();
    	}
    	
    	path=simplifyPath(path);
    	Node cur;
    	String[] parsedPath=path.split("/");
    	
    	try{
    		cur=findNodeOnPath(parsedPath,parsedPath.length-1);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("The specified path is invalid.");
        	return;
    	}
    	
    	try{
    		System.out.println(cur.getContent());
    		cur.setLastAccessed(System.currentTimeMillis());
    	}
    	catch(UnsupportedOperationException u){
    		System.out.println("Read file operation not supported for this entry");
    	}
    }
    
    public void writefile(String path,String content) throws IllegalArgumentException,Exception{
    	if(path==null){
    		throw new IllegalArgumentException();
    	}
    	
    	path=simplifyPath(path);
    	Node cur;
    	String[] parsedPath=path.split("/");
    	
    	try{
    		cur=findNodeOnPath(parsedPath,parsedPath.length-1);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("The specified path is invalid.");
        	return;
    	}
    	
    	try{
    	    cur.setContent(content);
    	    cur.setLastAccessed(System.currentTimeMillis());
    	    cur.setLastUpdated(System.currentTimeMillis());
    	}
    	catch(UnsupportedOperationException u){
    		System.out.println("Write file operation not supported for this entry");
    	}
    }
   
    public void mkfile(String path,String content) throws IllegalArgumentException,Exception{
        if(path==null){
        	throw new IllegalArgumentException();
        }
        path=simplifyPath(path);
        Node cur;
        String[] parsedPath=path.split("/");
        try{
        	cur=findNodeOnPath(parsedPath,parsedPath.length-2);
        }
        catch(NoSuchElementException n){
        	System.out.println("The specified path is invalid.");
        	return;
        }
        if(!cur.addChild(new File(parsedPath[parsedPath.length-1],cur,content))){
        	throw new Exception();
        }
    }
    
    public void mkdir(String path) throws Exception{
    	if(path==null){
    		throw new IllegalArgumentException();
    	}
    	path=simplifyPath(path);
    	Node cur;
    	String[] parsedPath=path.split("/");
    	try{
        	cur=findNodeOnPath(parsedPath,parsedPath.length-2);
        }
        catch(NoSuchElementException n){
        	System.out.println("The specified path is invalid.");
        	return;
        }
        if(!cur.addChild(new Directory(parsedPath[parsedPath.length-1],cur))){
        	throw new Exception();
        }
    }
    /**
     * In the current implementation, the parameter fromPath can be either a directory or a file. If the fromPath is 
     * a directory, toPath cannot be a file. 
     * If the target doesn't exist, then the method creates a new file or directory with its name as specified by the toPath,
     * and its content the same as the Node's content indicated by fromPath.
     * @param fromPath
     * @param toPath
     * @throws Exception
     */
    public void cp(String fromPath,String toPath) throws Exception{
    	
    	fromPath=simplifyPath(fromPath);
    	toPath=simplifyPath(toPath);
    	
    	String[] parsedFrom=fromPath.split("/");
    	String[] parsedTo=toPath.split("/");
    	if(parsedFrom==null||parsedTo==null){
        	throw new Exception();
        }
    	
    	Node curFrom;
    	try{
    		curFrom=findNodeOnPath(parsedFrom,parsedFrom.length-1);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("Cannot find the SOURCE directory or file");
    		return;
    	}
    	
    	Node curTo;
    	try{
    		curTo=findNodeOnPath(parsedTo,parsedTo.length-2);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("Cannot find the DESTINATION directory or file");
    		return;
    	}
    	
    	if(curTo.containsChild(parsedTo[parsedTo.length-1])){
    		curTo=curTo.getChild(parsedTo[parsedTo.length-1]);
    		if(curTo instanceof Directory){
    			curTo.addChild(curFrom.copyNode());
    		}
    		else if(curFrom instanceof File){
    			((File)curTo).content=((File)curFrom).content;
    		}
    		else{
    			throw new Exception();
    		}
    	}
    	else{
    		if(curTo instanceof File){
    			System.out.println("Cannot create file or directory under a file");
    			throw new Exception();
    		}
    		else{
    			curTo.addChild(curFrom.copyNode(parsedTo[parsedTo.length-1]));
    		}
    	}
    	
    	curFrom.setLastAccessed(System.currentTimeMillis());
    	curTo.setLastAccessed(System.currentTimeMillis());
    	curTo.setLastUpdated(System.currentTimeMillis());
    }
    /**
     * In this implementation, fromPath and toPath can "point to" either a File or a Directory. However, fromPath being a 
     * Directory and toPath being a File is not allowed. If the place indicated by either fromPath or toPath doesn't exist,
     * the method throws an Exception. 
     * @param fromPath
     * @param toPath
     * @throws Exception
     */
    public void mv(String fromPath,String toPath) throws Exception{
    	String[] parsedFrom=fromPath.split("/");
    	String[] parsedTo=toPath.split("/");
    	if(parsedFrom==null||parsedTo==null){
        	throw new Exception();
        }
    	
    	Node curFrom;
    	try{
    		curFrom=findNodeOnPath(parsedFrom,parsedFrom.length-1);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("Cannot find the SOURCE directory or file");
    		return;
    	}
    	
    	Node curTo;
    	try{
    		curTo=findNodeOnPath(parsedTo,parsedTo.length-2);
    	}
    	catch(NoSuchElementException n){
    		System.out.println("Cannot find the DESTINATION directory or file");
    		return;
    	}
    	
    	if(curTo.containsChild(parsedTo[parsedTo.length-1])){
    		curTo=curTo.getChild(parsedTo[parsedTo.length-1]);
    		if(curTo instanceof Directory){
    			curFrom.delete();
    			curTo.addChild(curFrom);
    		}
    		else if(curFrom instanceof Directory){
    			throw new Exception();
    		}
    		else{
    			curTo.parent.addChild(curFrom);
    			curFrom.delete();
    			curTo.delete();
    		}
    	}
    	else{
    		if(curTo instanceof File){
    			System.out.println("Cannot create file or directory under a file");
    			throw new Exception();
    		}
    		else{
    			curFrom.name=parsedTo[parsedTo.length-1];
    			curTo.addChild(curFrom);
    			curFrom.delete();
    		}
    	}
    	
    	curFrom.setLastAccessed(System.currentTimeMillis());
    	curTo.setLastAccessed(System.currentTimeMillis());
    	curTo.setLastUpdated(System.currentTimeMillis());
    }
}

