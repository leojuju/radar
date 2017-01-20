package infrastruction.radarprocess.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MataDatas {
	
	private static Logger logger = LoggerFactory.getLogger(MataDatas.class);
	
	public static final Map<String,MataData> mataDatas=new HashMap<String,MataData>();
	public static final String MataDataFile="properties/stationsMatadatas";
	
	public static void init(String contentPath){
		BufferedReader br4mataData =null;
		try {
			br4mataData = new BufferedReader(new InputStreamReader(
					new FileInputStream(contentPath+"/"+MataDataFile), "UTF-8"));
			br4mataData.readLine();
			String line = null;
			while((line=br4mataData.readLine())!=null){
				String[] strs = line.split(",");
				int w = Integer.valueOf(strs[1]);
				int h = Integer.valueOf(strs[2]);
				double latScale = Double.valueOf(strs[3]);
				double longtScale = Double.valueOf(strs[4]);
				LatAndLongt startLAL = new LatAndLongt(Double.valueOf(strs[5]), Double.valueOf(strs[6]));
				LatAndLongt endLAL = new LatAndLongt(Double.valueOf(strs[7]), Double.valueOf(strs[8]));
				mataDatas.put(strs[0], new MataData(strs[0], w, h, latScale, longtScale, startLAL, endLAL));
			}
			
		} catch (Exception e) {
			File file = new File(contentPath+"/"+MataDataFile);
			logger.error(file.getAbsolutePath()+" MataDatas for stations load failed,system exit 1.");
			System.exit(1);
		}finally {
			try {
				br4mataData.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("MataDatas for stations properties successfully loaded.");
	}
	
	public static void save(){
		PrintWriter pw = null;
		try {
			File file = new File(MataDataFile);
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			pw = new PrintWriter(new FileOutputStream(file,true), true);
			List<String> stationNames = new ArrayList<>(mataDatas.keySet());
			Collections.sort(stationNames);
			
			pw.println(MataData.fieldNamesToString());
			for (String stationName:stationNames) {
				MataData matadata = mataDatas.get(stationName);
				pw.print(stationName+",");
				pw.print(matadata.h+",");
				pw.print(matadata.w+",");
				pw.print(matadata.latScale+",");
				pw.print(matadata.longtScale+",");
				pw.print(matadata.startLAL.latitude+",");
				pw.print(matadata.startLAL.longtitude+",");
				pw.print(matadata.endLAL.latitude+",");
				pw.print(matadata.endLAL.longtitude);
				pw.println();
			}
		} catch (Exception e) {
		}finally {
			pw.flush();
			pw.close();
		}
	}
}
