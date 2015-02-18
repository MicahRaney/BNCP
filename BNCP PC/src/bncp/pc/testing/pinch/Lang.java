package bncp.pc.testing.pinch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Lang {

	/**
	 * True if the resource file has been loaded. False if not.
	 */
	private static volatile boolean loaded = false;
	private static HashMap<String, String> values = new HashMap<String, String>();
	
	public static boolean load(){
		return load("lang.txt");
	}
	
	
	public static boolean load(String filename){
	
		
		File langFile = new File(filename);
		
		if(!langFile.exists() || langFile.isDirectory()){
			System.out.println("The Lang file (" +  filename + ") does not exist!");
			return false;
		}
		
		Scanner filein = null;
		
		try {
			
			loaded = false;
			
			values.clear();//clear old lang.
			
			filein = new Scanner(new FileInputStream(langFile));
			int c;
			for(c = 0; filein.hasNext(); c++){//parse all the lines.
				String line = filein.nextLine();
				values.put(parseName(line).toLowerCase(), parseValue(line));
				//System.out.println("Loaded: " + parseName(line) + " --as-- " + parseValue(line));
				//store the name as the key and store the value in the HashMap.
			}
			
			System.out.println("Lang parsed " + c + " lines from " + filename + ".");
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found!?!?! But it exists and isn't directory... File: " + filename);
			e.printStackTrace();
			return false;
		} finally{
			if(filein != null)
				filein.close();
		}

		loaded = true;
		
		return true;
	}
	
	private static String parseName(String line){
		int ind = line.indexOf('=');//where is the equal sign?
		
		if(ind == -1)
			return line;
		else
			return line.substring(0, ind);
	}
	private static String parseValue(String line){
		int ind = line.indexOf('=');
		
		if(ind == -1)
			return "";
		else
			return line.substring(ind + 1, line.length());
	}
	
	public static String getValue(String key){
		if(!loaded)//is it loaded?
			if(!load())//try loading
				throw new IllegalStateException("Lang can't load!?");//it still can't load!
		
		String ret = values.get(key.toLowerCase());
		if(ret == null)
			return "";//if its null, just return a blank
		else
			return ret;
	}
	
}
