package infrastruction;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

	public static final String WEBURL = "https://www.baidu.com/";
	
	public static String getRealDateOnGMT(){
        try {
            URL url = new URL(WEBURL);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.setConnectTimeout(10000);
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld-8*60*60000);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (Exception e) {
        }
        return null;
    }
	
	public static String getRealDateInChina(){
        try {
            URL url = new URL(WEBURL);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.setConnectTimeout(10000);
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (Exception e) {
        }
        return null;
    }
	
	public static String getRealTimeOnGMT(){
        try {
            URL url = new URL(WEBURL);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.setConnectTimeout(10000);
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld-8*60*60000);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (Exception e) {
        }
        return null;
    }
	
	public static String getRealTimeInChian(){
        try {
            URL url = new URL(WEBURL);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.setConnectTimeout(10000);
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (Exception e) {
        }
        return null;
    }

	public static String getRealDateOnGMTFromLocalSystem(){
		Calendar c =Calendar.getInstance();
		long offset  = c.getTimeZone().getRawOffset();
		Date date = new Date(c.getTimeInMillis()-offset);
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	
	public static String getRealTimeOnGMTFromLocalSystem(){
		Calendar c =Calendar.getInstance();
		long offset  = c.getTimeZone().getRawOffset();
		Date date = new Date(c.getTimeInMillis()-offset);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
}
