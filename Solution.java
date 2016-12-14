import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/**
 * 
 * How To run:
 * 
 * javac Solution.java
 * 
 * run with command line :
 * java Solution
 * 
 * run with input file:
 * java Solution inputfilename
 * 
 * Time Complexity:
 * SET/UNSET/GET/NUMEQUALTO/BEGIN/COMMIT O(1) 
 * ROLLBACK O(k), k is the number of commands in a transaction
 * 
 * Space Complexity:
 * O(n): use 2 HashMap + 1 stack
 */


public class Solution {
	
	public static SimpleDB simpleDB;
	
	/**
	 * Read from file input 
	 */
	public static void readFromFile(String fileName) {
		InputStream is = null; 
	    InputStreamReader isr = null;
	    BufferedReader br = null;
	    
		String line = null;
		try{
			// open input stream test.txt for reading purpose.
			is = new FileInputStream(fileName);	         
	        // create new input stream reader
			isr = new InputStreamReader(is);	         
	        // create new buffered reader
			br = new BufferedReader(isr);
	        
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	            simpleDB.handleCommandLine(line);
	        }       
	     }catch(IOException e){
	         e.printStackTrace();
	     }
	}
	
	
	/**
	 * Read from user input	  
	 * 
	 */
	public static void readFromCommandLine() {
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));			
			while ((line = br.readLine()) != null) {
	            System.out.println(line);
	            simpleDB.handleCommandLine(line);
	         }  
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	
    public static void main(String args[] ) throws Exception {
    	simpleDB = new SimpleDB();
    	
    	try {
    		if (args.length == 0)    			
    		    readFromCommandLine();
    		else if (args.length == 1)
    			readFromFile(args[0]);
    	}catch (Exception e) {
    		e.printStackTrace();
    	}    	
    }
}
