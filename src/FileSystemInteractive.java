import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;


public class FileSystemInteractive {
	public static void main(String[] args){
		
	     FileSystem system;
	   	 try{
	   		 system=new FileSystem();
	   	 }
	   	 catch(Exception e){
	   		 e.printStackTrace();
	   		 System.out.println("Cannot initiate OOFileSystem");
	   		 return;
	   	 }
	   	 
	   	Scanner scan=new Scanner(System.in);
	    try{
		    while(true){
		    	
		    	String command;
		    	 try{
		    		 command=scan.nextLine();
		    	 }
		    	 catch(Exception e){
		 	   		e.printStackTrace();
		 	   		System.out.println("Cannot read the next line of input.");
		 	   		return;
		    	 }
		    	 
		    	 
		    	 if(command.equals("exit")){
		    		 return;
		    	 }
		    	 
		    	 String[] parsedCommand=command.split("(?<!\\\\)\\s+");
		    	 for(int i=0;i<parsedCommand.length;i++){
		    		 parsedCommand[i]=parsedCommand[i].replaceAll("\\\\ ", " ");
		    	 }
		    	 
		    	 
		    	 
		    	 
		    	 Class[] classArray=new Class[parsedCommand.length-1];
		    	 Arrays.fill(classArray, command.getClass());
		    	 	
		    	 Method operation;
		    	 try{
		    		 operation=system.getClass().getMethod(parsedCommand[0],classArray);
		    	 }
		    	 catch(SecurityException s){
		    		 s.printStackTrace();
		    		 System.out.println("Cannot execute command becasue of security concern");
		    		 continue;
		    	 }
		    	 catch(NoSuchMethodException n){
		    		 System.out.println("Command not correct");
		    		 continue;
		    	 }
		    	 
		    	 String[] params=Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length);
		    	 try{
		    		 operation.invoke(system, params);
		    	 }
		    	 catch(Exception e){}
		    }
	    }
	    finally{
	    	scan.close();
	    }
	    
   	}
	   	
}
