package infrastruction.radarprocess;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import infrastruction.radarprocess.geo.G;
import infrastruction.radarprocess.geo.GeoUtils;
import infrastruction.radarprocess.geo.LatAndLongt;

public class RadarUtils {
		
	/**
	 * 获取处理雷达原图中的雷达图部分,并经过像素单一化.
	 * @param originImage
	 * @return
	 */
	public static BufferedImage getRadarRegion(BufferedImage originImage){
		int H =originImage.getHeight();
		int W=H;
		int[] matrix = new int[H*W];
		originImage.getRGB(0, 0, W, H, matrix, 0, W);
		BufferedImage result = new BufferedImage(W, H, originImage.getType());
		matrix=filterMatrix(matrix,W,H);
		matrix=substractLines(matrix,W,H);
		result.setRGB(0, 0, W, H, matrix, 0, W);
		return result;
	}	
	
	/**
	 * 获取处理雷达原图中的雷达图部分,并经过像素单一化,和简化成数组的形式
	 * @param originImage
	 * @return
	 */
	public static byte[] getRadarRegionByteArray(BufferedImage originImage){
		int H =originImage.getHeight();
		int W=H;
		int[] matrix = new int[H*W];
		originImage.getRGB(0, 0, W, H, matrix, 0, W);
		matrix=filterMatrix(matrix,W,H);
		matrix=substractLines(matrix,W,H);
		return simplefy(matrix);
	}	
	
	/**
	 * 处理雷达原图中的雷达图部分,过滤去背景图案。
	 * @param matrix
	 * @param w
	 * @param h
	 * @return
	 */
	public static int[] filterMatrix(int[] matrix, int w, int h){
		//去掉中间十字
		int midy = h/2-1;
		int midx =w/2;
		for (int i = -4; i < 5; i++) {
			for (int j = -4; j < 5; j++) {
				if(i==0||j==0){
					matrix[(i+midy)*w+(j+midx)]=0xffaaaaaa;
				}
			}
		}
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int index = i * w + j;
				int t = matrix[index];
				switch (t) {
				case 0xffd8d8d8:
				case 0xff0000f6:
				case 0xff01a0f6:
				case 0xff00ecec:
				case 0xff01ff00:
				case 0xff00c800:
				case 0xff019000:
				case 0xffffff00:
				case 0xffe7c000:
				case 0xffff9000:
				case 0xffff0000:
				case 0xffd60000:
				case 0xffc00000:
				case 0xffff00f0:
				case 0xff780084:
				case 0xffad90f0:
					
				case 0xff206060:
				case 0xff000080:
				case 0xff73dfff:
				case 0xff00a9e6:
				case 0xff00c5ff:
				case 0xff4e4e4e:
				case 0xff097208:
				case 0xff098008:
				case 0xff04b603:
					
				case 0xffaaaaaa:
				case 0xffa8a8a8:
				case 0xff686868:
				case 0xffb2b2b2:
					break;
				default:
					matrix[index] = 0xffd8d8d8;
					break;
				}
			}
		}
		return matrix;
	}
	
	
	/**
	 * 处理雷达原图中的雷达图部分,去除地图线和地名
	 * @param matrix
	 * @param w
	 * @param h
	 * @return
	 */
	public static int[] substractLines(int[] matrix, int w, int h) {
		int[] temp = Arrays.copyOf(matrix, matrix.length);
		for (int i = 0; i < h; i++) {
			for(int j=0;j< w;j++){
				int index=i*w+j;
				int t=matrix[index];
				switch(t){
					case 0xff206060:
					case 0xff000080:
					case 0xff73dfff:
					case 0xff00a9e6:
					case 0xff00c5ff:
					case 0xff4e4e4e:
					case 0xff097208:
					case 0xff098008:
					case 0xff04b603:
						
					case 0xffaaaaaa:
					case 0xffa8a8a8:
					case 0xff686868:
					case 0xffb2b2b2:
						int result=0;
						int scale=1;
						while( (result=getMaxSurrounding(temp,i,j,w,h,scale))==0){
							scale++;
						}
						matrix[index]=result;
						break;
				}
			}
		}
		return matrix;
	}
	
	/**
	 * 检查(raw=i,col=j)处像素点周围(2*scale+1)*(2*scale+1)(排除自身)区域范围内的最多的颜色
	 * 针对的地图线和地名对应的像素点。
	 * @param matrix
	 * @param i
	 * @param j
	 * @param scale
	 * @return 最多的颜色对应的值
	 */
	public static int getMaxSurrounding(int[] matrix, int i, int j,int w,int h, int scale) {
		
		Map<Integer,Integer> colormap = new HashMap<Integer,Integer>();
		for (int k1 = -scale; k1 < scale+1; k1++) {
			for (int k2 = -scale; k2 < scale+1; k2++) {
				int a =i+k1;
				int b=j+k2;
				if(a>=0&&a<h&&b>=0&&b<w){
					int color = matrix[a*w+b];
					//排除地图线和地名对应的像素颜色
					switch(color){
					//gif
					case 0xff206060:
					case 0xff000080:
					//png
					case 0xff73dfff:
					case 0xff00a9e6:
					case 0xff00c5ff:
					case 0xff4e4e4e:
					//地名中间的点	
					case 0xff097208:
					case 0xff098008:
					case 0xff04b603:
						
					case 0xffaaaaaa:
					case 0xffa8a8a8:
					case 0xff686868:
					case 0xffb2b2b2:
						break;
					default :
						if(colormap.containsKey(color)){
							colormap.put(color, colormap.get(color)+1);
						}else{
							colormap.put(color, 1);
						}
						break;
					}
				}
			}
		}
		Set<Integer> keys = colormap.keySet();
		int maxKey=0;
		int maxValue=0;
		for(Integer key:keys){
			int value =colormap.get(key);
			if(value > maxValue){
				maxKey=key;
				maxValue=value;
			}
		}
		return maxKey;
	}
	
	
	public static long getImageTimeFromString(String[] infoStrs){
		return Long.valueOf(infoStrs[0]+infoStrs[1]+infoStrs[2]+infoStrs[3]+infoStrs[4]+infoStrs[5]);
	}
	
	public static ImageInfo getInfoFromStrings(String[] infoStrs){
		
		int year = Integer.valueOf(infoStrs[0]);
		int month = Integer.valueOf(infoStrs[1]);
		int day = Integer.valueOf(infoStrs[2]);
		int hour = Integer.valueOf(infoStrs[3]);
		int minute = Integer.valueOf(infoStrs[4]);
		int second = Integer.valueOf(infoStrs[5]);
		int range = Integer.valueOf(infoStrs[6]);
		
		return new ImageInfo(new Time(year, month, day, hour, minute, second), range, infoStrs[7]);
	}
	
	/**
	 * 获取数字信息,即时间信息以及数据范围相关的像素
	 * @param image
	 * @return
	 */
	public static int[] getDigitPixels(BufferedImage image){
		int xStart =549;
		int yStart= 49;
		if(image.getHeight()==512){
			xStart=581;
		}
		int w=7;
		int h=11;
		int[] buf = new int[23*w*h];
		//年月日
		for (int i = 0; i < 4; i++) {
			image.getRGB((w+1)*i+xStart, 0+yStart, w, h, buf, i*h*w, w);
		}
		for (int i = 0; i < 2; i++) {
			image.getRGB(40+(w+1)*i+xStart, 0+yStart, w, h, buf, (4+i)*h*w, w);
		}
		for (int i = 0; i < 2; i++) {
			image.getRGB(64+(w+1)*i+xStart, 0+yStart, w, h, buf, (6+i)*h*w, w);
		}
		//时分秒
		for (int i = 0; i < 2; i++) {
			image.getRGB((w+1)*i+xStart, 15+yStart, w, h, buf,(8+i)*h*w, w);
		}
		for (int i = 0; i < 2; i++) {
			image.getRGB(24+(w+1)*i+xStart, 15+yStart, w, h, buf, (10+i)*h*w, w);
		}
		for (int i = 0; i < 2; i++) {
			image.getRGB(48+(w+1)*i+xStart, 15+yStart, w, h, buf, (12+i)*h*w, w);
		}
		//数据范围
		for (int i = 0; i < 3; i++) {
			image.getRGB((w+1)*i+xStart, 35+yStart, w, h, buf, (14+i)*h*w, w);
		}
		//仰角
		for (int i = 0; i < 6; i++) {
			image.getRGB((w+1)*2*i+xStart, 52+yStart, w, h, buf, (17+i)*h*w, w);
		}
		return buf;
	}	
	
	
	public static int[] simplefyDigits(int[] digits){
		int[] results = new int[digits.length];
		for (int i = 0; i < digits.length; i++) {
			if(digits[i]==0xffffffff){
				results[i]=1;
			}else if(digits[i]==0xff202020){
				results[i]=-1;
			}
		}
		return results;
	}
	
	
	public static byte[] simplefy(BufferedImage newImage){
		int w = newImage.getWidth();
		int h = newImage.getHeight();
		byte[] b = new byte[w*h];		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int index=i*w+j;
				int t=newImage.getRGB(j, i);
				switch(t){
				case 0xffd8d8d8:
					b[index]=0;
					break;
				case 0xff0000f6:
					b[index]=1;
					break;
				case 0xff01a0f6:
					b[index]=2;
					break;
				case 0xff00ecec:
					b[index]=3;
					break;
				case 0xff01ff00:
					b[index]=4;
					break;
				case 0xff00c800:
					b[index]=5;
					break;
				case 0xff019000:
					b[index]=6;
					break;
				case 0xffffff00:
					b[index]=7;
					break;
				case 0xffe7c000:
					b[index]=8;
					break;
				case 0xffff9000:
					b[index]=9;
					break;
				case 0xffff0000:
					b[index]=10;
					break;
				case 0xffd60000:
					b[index]=11;
					break;
				case 0xffc00000:
					b[index]=12;
					break;
				case 0xffff00f0:
					b[index]=13;
					break;
				case 0xff780084:
					b[index]=14;
					break;
				case 0xffad90f0:
					b[index]=15;
					break;
				}
			}
		}
		return b;
	}
	
	
	public static byte[] simplefy(int[] matrix){
		byte[] b = new byte[matrix.length];		
		for (int i = 0; i < matrix.length; i++) {
				int t= matrix[i];
				switch(t){
				case 0xffd8d8d8:
					b[i]=0;
					break;
				case 0xff0000f6:
					b[i]=1;
					break;
				case 0xff01a0f6:
					b[i]=2;
					break;
				case 0xff00ecec:
					b[i]=3;
					break;
				case 0xff01ff00:
					b[i]=4;
					break;
				case 0xff00c800:
					b[i]=5;
					break;
				case 0xff019000:
					b[i]=6;
					break;
				case 0xffffff00:
					b[i]=7;
					break;
				case 0xffe7c000:
					b[i]=8;
					break;
				case 0xffff9000:
					b[i]=9;
					break;
				case 0xffff0000:
					b[i]=10;
					break;
				case 0xffd60000:
					b[i]=11;
					break;
				case 0xffc00000:
					b[i]=12;
					break;
				case 0xffff00f0:
					b[i]=13;
					break;
				case 0xff780084:
					b[i]=14;
					break;
				case 0xffad90f0:
					b[i]=15;
					break;
				}
		}
		return b;
	}
	
	
	
	public static BufferedImage unsimplefy(byte[] matrix,int w,int h){
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int index=i*w+j;
				byte t=matrix[index];
				int r=0;
				switch(t){
//				case -1:
				case 0:
					r=0;
					break;
				case 1:
					r=0xff0000f6;
					break;
				case 2:
					r=0xff01a0f6;
					break;
				case 3:
					r=0xff00ecec;
					break;
				case 4:
					r=0xff01ff00;
					break;
				case 5:
					r=0xff00c800;
					break;
				case 6:
					r=0xff019000;
					break;
				case 7:
					r=0xffffff00;
					break;
				case 8:
					r=0xffe7c000;
					break;
				case 9:
					r=0xffff9000;
					break;
				case 10:
					r=0xffff0000;
					break;
				case 11:
					r=0xffd60000;
					break;
				case 12:
					r=0xffc00000;
					break;
				case 13:
					r=0xffff00f0;
					break;
				case 14:
					r=0x780084;
					break;
				case 15:
					r=0xffad90f0;
					break;
				}
				image.setRGB(j, i, r);
			}
		}
		return image;
	}


	public static BufferedImage unsimplefy(byte[][] matrix, int w, int h) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				byte t=matrix[i][j];
				int r=0;
				switch(t){
				case 0:
					r=0xffd8d8d8;
					break;
				case 1:
					r=0xff0000f6;
					break;
				case 2:
					r=0xff01a0f6;
					break;
				case 3:
					r=0xff00ecec;
					break;
				case 4:
					r=0xff01ff00;
					break;
				case 5:
					r=0xff00c800;
					break;
				case 6:
					r=0x019000;
					break;
				case 7:
					r=0xffffff00;
					break;
				case 8:
					r=0xffe7c000;
					break;
				case 9:
					r=0xffff9000;
					break;
				case 10:
					r=0xffff0000;
					break;
				case 11:
					r=0xffd60000;
					break;
				case 12:
					r=0xffc00000;
					break;
				case 13:
					r=0xffff00f0;
					break;
				case 14:
					r=0x780084;
					break;
				case 15:
					r=0xffad90f0;
					break;
				}
				image.setRGB(j, i, r);
			}
		}
		return image;
	}
	
	public static byte[] geoProcess(BufferedImage radarImage,String[] infoStrs,String stationName){
		
		ImageInfo info = getInfoFromStrings(infoStrs);
		int dim = radarImage.getWidth();
		Pixel center = ImageFormat.getCenterPixel(dim);
		LatAndLongt centerLAL = Stations.stations.get(stationName).location;
		double scale = ImageFormat.getScale(dim, info.range);
		double dLat = G.latChange1KmAlongLongt*scale;
		double dLongt = centerLAL.getLongtChange1KmAlongLat()*scale;		
		byte[] matrix= simplefy(radarImage);
		byte[] transformedMatrix = transformToLalGrid(matrix, center, centerLAL, scale, dim,dLat,dLongt,info.range);
		return transformedMatrix;
		
	}

	public static byte[] geoProcess(byte[] radarBytes,int dim,ImageInfo info,String stationName){
		
		Pixel center = ImageFormat.getCenterPixel(dim);
		LatAndLongt centerLAL = Stations.stations.get(stationName).location;
		double scale = ImageFormat.getScale(dim, info.range);
		double dLat = G.latChange1KmAlongLongt * scale;
		double dLongt = centerLAL.getLongtChange1KmAlongLat() * scale;
		byte[] transformedMatrix = transformToLalGrid(radarBytes, center, centerLAL, scale, dim, dLat, dLongt,
				info.range);
		return transformedMatrix;
	}
	
	public static byte[] transformToLalGrid(byte[] matrix,Pixel center,LatAndLongt centerLAL,double scale,int dim,double dLat,double dLongt,int range){
		
		byte[] result = new byte[dim*dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				LatAndLongt point = new LatAndLongt(centerLAL.latitude-(i-center.y)*dLat, centerLAL.longtitude+(j-center.x)*dLongt);
				Pixel newPixel = GeoUtils.getPixelLocationRelativeTo(centerLAL, scale, point);
				int x = center.x+newPixel.x;
				int y = center.y+newPixel.y;
				double d = ((newPixel.x*newPixel.x)+(newPixel.y*newPixel.y))*scale*scale;
				if(d>range*range){
					result[i*dim+j]=-1;
				}else{
					try {
						result[i*dim+j]=matrix[y*dim+x];
					} catch (Exception e) {
						result[i*dim+j]=-1;
					}
				}
			}
		}
		return result;
	}
}
