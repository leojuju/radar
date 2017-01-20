package infrastruction;

import org.json.JSONObject;

public class TestJSON {
	public static void main(String[] args) {
		JSONObject j = new JSONObject();
		JSONObject j2 = new JSONObject();
		j.put("num", 12313123333331l);
		j.put("link", "www.baidu.com");
		j2.put("num", 12313123333331l);
		j2.put("link", "www.baidu.com");
		j.put("j", j2);
		System.out.println(j.toString());
		System.out.println(j.get("1312"));
		System.out.println(j.toString(5));
	}
}
