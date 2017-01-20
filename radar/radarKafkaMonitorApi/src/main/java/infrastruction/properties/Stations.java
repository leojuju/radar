package infrastruction.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Stations {
	
	public static Map<String,String> stationIDs = new HashMap<String,String>();
	public static Map<String,String> stationNames = new HashMap<String,String>();
	public static final String FilePath = "properties/stationIDs";
	
	public static void init(String dirPath){
		BufferedReader br4location =null;
		System.out.println("Loading stations url properties.");
		try {
			String line = null;
			//读取location信息
			br4location = new BufferedReader(new InputStreamReader(
					new FileInputStream(dirPath+"/"+FilePath), "UTF-8"));
			br4location.readLine();
			while((line=br4location.readLine())!=null){
				String[] strs = line.split(",");
				stationIDs.put(strs[0], strs[1]);
				stationNames.put(strs[1], strs[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			File file = new File(dirPath+"/"+FilePath);
			System.out.println(file.getAbsolutePath()+" Stations properties load failed!Application exit 0.");
			System.exit(0);
		}finally {
			try {
				if(br4location!=null){
					br4location.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stations properties loaded.");
	}
	
	public static boolean exist(String station) {
		return stationIDs.get(station)!=null;
	}

	public static Set<String> getStationIDs() {
		return stationNames.keySet();
	}

}
