import java.util.*;


//import SimpleDB.Command;
public class SimpleDB {
	//name_value map
	private HashMap<String, Integer> nameValue;
	//value_count map
	private HashMap<Integer, Integer> valueCount;
	//ROLLBACK Stack
	private Deque<String> rollbackStack;
	//# of Transaction blocks (begin)
	private int numBlocks;
	
	public SimpleDB(){
		nameValue = new HashMap<String,Integer>();
		valueCount = new HashMap<Integer,Integer>();
		rollbackStack = new ArrayDeque<String>();
		numBlocks = 0;
	}
	
	
	/**
	 * Handle command line
	 * To make it simple, assume all input format are valid here
	 */ 	
	public void handleCommandLine(String line) {
		String[] tokens = line.split("\\s+");	
		String cmd = tokens[0];
		String name;
		int value;
		boolean isTransaction;
		switch (cmd.toUpperCase()) {
			case "SET":				
				name = tokens[1];
				value = Integer.parseInt(tokens[2]);
				isTransaction = numBlocks == 0? false: true;
				set(name, value, isTransaction);				
				break;
			
			case "GET":				
				name = tokens[1];
				get(name);
				break;
			
			case "UNSET":
				name = tokens[1];
				isTransaction = numBlocks == 0? false: true;
				unset(name,isTransaction);				
				break;
			
			case "NUMEQUALTO":
				value = Integer.parseInt(tokens[1]);
				numEqualTo(value);
				break;
			
			case "BEGIN":				
				begin();
				break;
			
			case "ROLLBACK":
				rollback();				
				break;
			
			case "COMMIT":			
				commit();
				break;
			
			case "END":
				System.exit(0);
			
			default:
				System.err.println("Invalid input: " + line + "\n");
				break;	
			
		}
	}
	
		
	public void set(String name, int value, boolean isTransaction){		
		//count(original value) -1
		if(nameValue.containsKey(name)){
			int oldValue = nameValue.get(name);
			valueCount.put(nameValue.get(name), valueCount.get(nameValue.get(name))-1);
			//update new name value
			nameValue.put(name, value);
			//add to rollbackStack
			if(isTransaction){
				String rollbackCommand = "SET " + name + " " + Integer.toString(oldValue);
				rollbackStack.push(rollbackCommand);
			}
		}else{
			nameValue.put(name, value);
			if(isTransaction){
				String rollbackCommand = "UNSET " + name;
				rollbackStack.push(rollbackCommand);
			}
		}
		
		//count(new value) + 1
		if(valueCount.containsKey(value)){
			valueCount.put(value, valueCount.get(value)+1);
		}else{
			valueCount.put(value,1);	
		}
		
	}
	
	
	public void get(String name){
		if(nameValue.containsKey(name)){
			System.out.println("> " + nameValue.get(name));
		}else{
			System.out.println("> " + "NULL");
		}
		
	}
	
	
	public void unset(String name, boolean isTransaction){
		if(nameValue.containsKey(name)){
			int value = nameValue.get(name);
			nameValue.remove(name);
			valueCount.put(value, valueCount.get(value)-1);
			if(isTransaction){
				String rollbackCommand = "SET " + name + " "+ Integer.toString(value);
				rollbackStack.push(rollbackCommand);
			}
		}
	}
		
	public void numEqualTo(int value){
		if(valueCount.containsKey(value)){
			System.out.println("> " + valueCount.get(value));
		} else{
			System.out.println("> " + 0);
		}
	}
	
	public void begin() {
		numBlocks++;
		rollbackStack.push("BEGIN");
	}
	
	public void rollback() {
		if(rollbackStack.isEmpty()){
			System.out.println("> " + "NO TRANSACTION");
			return;
		}
		String rollbackCommand = rollbackStack.pop();
		while(!rollbackCommand.equals("BEGIN")){
			String[] tokens = rollbackCommand.split("\\s+");
			if(tokens[0].equals("SET")){
				set(tokens[1], Integer.parseInt(tokens[2]), false);
			}
			if(tokens[0].equals("UNSET")){
				unset(tokens[1],false);
			}
			rollbackCommand = rollbackStack.pop();
		}
		numBlocks--;
	}
	
	public void commit() {
		if(numBlocks==0){
			System.out.println("> " + "NO TRANSACTION");
			return;
		}
		rollbackStack.clear();
		numBlocks = 0;
	}
}