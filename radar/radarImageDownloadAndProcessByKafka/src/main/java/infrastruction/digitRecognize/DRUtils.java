package infrastruction.digitRecognize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DRUtils {
	
	private static Logger logger = LoggerFactory.getLogger(DRUtils.class);
	
	public static OneVersusAllDecomposition OAD = null;
	
	public static void init(){
		OAD = loadOADFromArray("data/oad.byte");
		logger.info("OAD successfully loaded.");
	}
	
	public static int recognize(double[] input) {
		return OAD.test(input).result;
	}
	
	public static OneVersusAllDecomposition loadOAD(String string) {
		File file  = new File(string);
		OneVersusAllDecomposition oad = null;
		if(!file.exists()){
			logger.error("Digit recognizer OAD load failed,system exit 1");
			System.exit(1);
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			oad=(OneVersusAllDecomposition) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return oad;
	}
	public static void saveOAD(OneVersusAllDecomposition oad) {
		ObjectOutputStream oos = null;
		try {
			File file = new File("oad.obj");
			 oos= new ObjectOutputStream(new FileOutputStream(file,false));
			 oos.writeObject(oad);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static OneVersusAllDecomposition loadOADFromArray(String string) {
		File file  = new File(string);
		OneVersusAllDecomposition oad = null;
		if(!file.exists()){
			logger.error("Digit recognizer OAD load failed,system exit 1");
			System.exit(1);
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			oad = new OneVersusAllDecomposition(10, 77, 0.1);
			LogisticRegressionPerceptron[] lrps = oad.getLrps();
			for (int i = 0; i < 10; i++) {
				double[] weight = (double[])ois.readObject();
				lrps[i].setWeight(weight);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Digit recognizer OAD load failed,system exit 1");
			System.exit(1);
		} finally{
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return oad;
	}
	
	public static void saveOAD2Array(OneVersusAllDecomposition oad) {
		ObjectOutputStream oos = null;
		try {
			File file = new File("oad.byte");
			oos= new ObjectOutputStream(new FileOutputStream(file,true));
			LogisticRegressionPerceptron[] lrps = oad.getLrps();
			for(LogisticRegressionPerceptron lrp:lrps ){
				oos.writeObject(lrp.getWeight());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void print(byte[] b) {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print(b[i * 7 + j] == -1 ? "@" : " ");
			}
			System.out.println();
		}
	}
	public  static double[] getInput(byte[] b) {
		double[] input = new double[b.length];
		for (int i = 0; i < input.length; i++) {
			input[i]=(double)b[i];
		}
		return input;
	}

}
