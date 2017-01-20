package infrastruction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
	
	private static final int buff_length = 16*1024;
	
	public static byte[] compress(byte[] data) throws IOException{
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		GZIPOutputStream gos = null;
		gos = new GZIPOutputStream(bos);
		int count=-1;
		byte[] buff = new byte[buff_length];
		while((count=bis.read(buff))!=-1){
			gos.write(buff, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
		
		byte[] result = bos.toByteArray();
		bos.flush();
		bos.close();
		bis.close();
		
		return result;
	}
	
	public static byte[] decompress(byte[] data) throws IOException{
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		GZIPInputStream gis =  new GZIPInputStream(bis);
		int count=-1;
		byte[] buff = new byte[buff_length];
		while((count=gis.read(buff))!=-1){
			bos.write(buff, 0, count);
		}

		gis.close();
		
		byte[] result = bos.toByteArray();
		
		bos.flush();
		bos.close();
		bis.close();
		
		return result;
	}
	
}
