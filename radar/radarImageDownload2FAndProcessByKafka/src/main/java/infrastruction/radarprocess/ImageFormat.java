package infrastruction.radarprocess;

public class ImageFormat {
	
	public static final Pixel center480 = new Pixel(240, 239);
	public static final Pixel center512 = new Pixel(256, 255);
	
	
	public static Pixel getCenterPixel(int dim){
		switch(dim){
		case 480:
			return center480;
		case 512:
			return center512;
		}
		return null;
	}
	
	public static double getScale(int dim,int range){
		switch(dim){
		case 480:
			if(range==230||range==200){
				return 1.0;
			}else if(range>230){
				return 2.0;
			}
		case 512:
			if(range==125){
				return 0.5;
			}else if(range==150){
				return 0.6;
			}
		}
		return 0;
	}
	
	
	public static int getRange(int dim,int range){
		switch(dim){
		case 480:
			if(range>230){
				return 460;
			}
		case 512:
		}
		return range;
	}
}
