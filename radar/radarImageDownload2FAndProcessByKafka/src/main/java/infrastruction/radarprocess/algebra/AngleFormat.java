package infrastruction.radarprocess.algebra;

public class AngleFormat {
	
	public static final String degree = "°";
	public static final String minute = "′";
	public static final String second = "″";
	
	public static String toAngleString(double d){
		int[] result = toAngleFormat(d);
		return result[0]+degree+result[1]+minute+result[2]+second;
	}
	
	public static int[] toAngleFormat(double d){
		int[] result = new int[3];
		int i =(int)(d*3600);
		result[2]=i%60;
		i/=60;
		result[1]=i%60;
		i/=60;
		result[0]=i;
		return result;
	}
}
