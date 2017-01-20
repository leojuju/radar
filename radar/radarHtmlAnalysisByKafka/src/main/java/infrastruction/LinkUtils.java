package infrastruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.db.RadarMongoDao;

public class LinkUtils {
	
	private static Logger logger = LoggerFactory.getLogger(LinkUtils.class);

	private static final String LinkRegexSmall ="http://image.nmc.cn/product/[0-9]{4}/[0-9]{2}/[0-9]{2}/RDCP/small/SEVP_AOC_RDCP_SLDAS_EBREF_AZ[0-9]{4}_L88_PI_[0-9]{12}00000.{4}";
	private static final String LinkRegexMedium ="http://image.nmc.cn/product/[0-9]{4}/[0-9]{2}/[0-9]{2}/RDCP/medium/SEVP_AOC_RDCP_SLDAS_EBREF_AZ[0-9]{4}_L88_PI_[0-9]{12}00000.{4}";
	public static Pattern pattern4Small =Pattern.compile(LinkRegexSmall,Pattern.CASE_INSENSITIVE);
	public static Pattern pattern4Medium =Pattern.compile(LinkRegexMedium,Pattern.CASE_INSENSITIVE);
	public static Pattern pattern =Pattern.compile(LinkRegexMedium,Pattern.CASE_INSENSITIVE);
	public static final int SMALL = 1;
	public static final int MEDIUN = -1;
	
	public static void setLinkPickPattern(int patternID){
		pattern = patternID>=0?pattern4Small:pattern4Medium;
		logger.info("Set link pick pattern "+(patternID>=0?"SMALL":"MEDIUM"));
	}
	
	public static List<String> findSubStrings(String htmlSrc, Pattern pattern) {
		List<String> strs = new ArrayList<>();
		Matcher m = pattern.matcher(htmlSrc);
		while(m.find()){
			strs.add(m.group(0).trim());
		}
		return strs;
	}
	
	public static List<String> findImageLinks(String htmlSrc, Pattern pattern) {
		List<String> strs = new ArrayList<>();
		Matcher m = pattern.matcher(htmlSrc);
		while(m.find()){
			String orgLink = m.group(0).trim();
			strs.add(format(orgLink));
		}
		return strs;
	}
	
	public static String getTimeString(String imageLink){
		return imageLink.substring(imageLink.length()-21, imageLink.length()-9);
	}

	public static String findSubStringOnce(String htmlSrc, Pattern pattern) {
		Matcher m = pattern.matcher(htmlSrc);
		if(m.find()){
			return m.group(0).trim();
		}
		return null;
	}

	public static String format(String link) {
		String[] strs = link.split("small/");
		if(strs.length<2){
			strs=link.split("medium/");
		}
		return strs[0]+strs[1];
	}
	

	public static List<String> filter(String stationID,List<String> imageLinks) {
		List<String> result = new ArrayList<>();
		for (String link:imageLinks) {
			long time = Long.valueOf(LinkUtils.getTimeString(link));
			if(!RadarMongoDao.isImageExist(stationID, time)){
				result.add(link);
			}
		}
		return result;
	}

	public static Map<String, String> getImageLinkMsgs(String stationID,
			JSONObject linkMsgValue, List<String> imageLink4Download) {
		Map<String, String> result = new HashMap<>();
		for(String link :imageLink4Download){
			JSONObject value = new JSONObject(linkMsgValue);
			value.remove("htmlLink");
			String time = getTimeString(link);
			value.put("imageLink", link);
			value.put("imageTime", time);
			result.put(stationID+"/"+time,value.toString());
		}
		return result;
	}
}
