package infrastruction;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static BufferedImage readImageFromBytes(byte[] array) {
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(array);
			return ImageIO.read(bais);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(bais!=null){
					bais.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] writeImageToBytes(BufferedImage image) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(image, "gif", baos);
			baos.flush();
			byte[] result = baos.toByteArray();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
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
