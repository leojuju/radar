package infrastruction.imageLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;


public class FileLogger implements Logger {

	public static String LogSavePath = "log/";
	
	public FileLogger() {
	}

	public static void init(String contentPath) {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			fis = new FileInputStream(contentPath+"/properties/log.properties");
			props.load(fis);
			LogSavePath = props.getProperty("FileSavePath",LogSavePath);
		} catch (Exception e) {
			System.out.println("Properties for App load failed,use default save path:["+LogSavePath+"].");
		} finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Properties for App loaded,use save path:["+LogSavePath+"].");
	}
	
	@Override
	public boolean save(String stationID, long time) {
		String date = String.valueOf(time);
		String dirPath = LogSavePath+stationID
				+"/"+date.substring(0, 6)
				+"/";
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		PrintWriter ps = null;
		try {
			ps = new PrintWriter(new FileOutputStream(dirPath+stationID+"-"+date.substring(0, 8)+".log",true),true);
			ps.println(date);
			return true;
		} catch (Exception e) {
			System.out.println("Save download log failed.");
		} finally {
			if(ps!=null){
				ps.close();
			}
		}
		return false;
	}

}
