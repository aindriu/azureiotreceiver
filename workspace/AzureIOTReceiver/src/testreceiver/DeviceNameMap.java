package testreceiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DeviceNameMap {

	private static boolean isLoaded = false;
	private static HashMap<String, String> deviceNameMap = new HashMap<String, String>();
	
	public static void main(String[] args) {
		String x = DeviceNameMap.get("214AE6");
		DeviceNameMap.addDevice("abcdef", "Test");

	}

	public static String get(String string) {
		if (!isLoaded) load();
		
		
		return deviceNameMap.get(string);
	}

	public static void addDevice(String id, String name)
	{
		File f = new File("devices.csv");
		if (!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f,  true));
			bw.append(id + "," + name);
			bw.close();
			load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void load() {
		
		deviceNameMap.clear();
		File f = new File("devices.csv");
		if (!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(f));
			
			while(bf.ready())
			{
				String s = bf.readLine();
				String id;
				String name;
				id = s.substring(0, s.indexOf(','));
				name = s.substring(s.indexOf(",") + 1, s.length());
				deviceNameMap.put(id, name);
			}
			
			/*
			Iterator it = deviceNameMap.entrySet().iterator();
			while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(pair.getKey() + " = " + pair.getValue());
			        
			}
			*/
			
			isLoaded = true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
