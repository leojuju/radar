package infrastruction;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	private static final int BUF_SZIE = 4*1024;
	private static final int CONNECTTIMEOUT=1000;
	private static final int READTIMEOUT=5000;
	
	
	public static String getHtmlSrc(String link) {
		URL pageUrl = null;
		InputStream is = null;
		try {
			pageUrl = new URL(link);
			URLConnection con = pageUrl.openConnection();
			con.setUseCaches(false);
			con.setConnectTimeout(CONNECTTIMEOUT);
			con.setReadTimeout(READTIMEOUT);
			is = con.getInputStream();
			byte[] buf = new  byte[BUF_SZIE];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len=-1;
			while((len = is.read(buf))!=-1){
				baos.write(buf, 0, len);
			}
			String result = baos.toString("utf-8");
			return result;
		} catch (Exception e) {
			logger.info("Read htmlSrc exception from link:" +link);
		} finally{
			try {
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] getBytes(String link) {
		URL url = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			url = new URL(link);
			URLConnection con = url.openConnection();
			con.setUseCaches(false);
			con.setConnectTimeout(CONNECTTIMEOUT);
			con.setReadTimeout(READTIMEOUT);
			bis = new BufferedInputStream(con.getInputStream());
			baos = new ByteArrayOutputStream();
			byte[] buf = new byte[BUF_SZIE];
			int len = -1;
			while((len=bis.read(buf))!=-1){
				baos.write(buf, 0, len);
			}
			byte[] result = baos.toByteArray();
			return result;
		} catch (Exception e) {
			logger.info("Read image bytes exception from link:"+link);
		} finally{
			try {
				if(bis!=null){
					bis.close();
				}
				if(baos!=null){
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
