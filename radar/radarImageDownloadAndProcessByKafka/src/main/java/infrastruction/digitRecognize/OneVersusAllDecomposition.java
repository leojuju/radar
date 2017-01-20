package infrastruction.digitRecognize;

import java.io.Serializable;


public class OneVersusAllDecomposition implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogisticRegressionPerceptron[] lrps;
	//number of classification
	private int classNum=0;
	private static final double threshold=0.5;
	/**
	 *  d��feature dimension. classNum: number of classification,decided how many perceptron we need.
	 * @param classNum
	 * @param d
	 */
	public OneVersusAllDecomposition(int classNum,int d){
		this.classNum=classNum;
		lrps = new LogisticRegressionPerceptron[classNum];
		for (int i = 0; i < lrps.length; i++) {
			lrps[i]=new LogisticRegressionPerceptron(d,i);
		}
	}
	public OneVersusAllDecomposition(int classNum,int d,double rate){
		this.classNum=classNum;
		lrps = new LogisticRegressionPerceptron[classNum];
		for (int i = 0; i < lrps.length; i++) {
			lrps[i]=new LogisticRegressionPerceptron(d,rate,i);
		}
	}
	
	
	public LogisticRegressionPerceptron[] getLrps() {
		return lrps;
	}
	
	/**
	 * һ��ѵ����targetҪ��ȡֵΪ0~classNum-1;
	 * @param input
	 * @param target
	 */
	public void train(double input[],int target){
		for (int i = 0; i < classNum; i++) {
			lrps[i].train(input, target==i?1:0);
		}
	}
	/**
	 * һ����ѵ��,target����Ҫ��ȡֵΪ0~classNum-1;
	 * @param input
	 * @param target
	 */
	public double trainBatch(double[][] input,int[] target){
		int m = input.length;
		double cost=0;
		for (int i = 0; i < classNum; i++) {
			//�������ӳ�䡣
			int y[]=new int[m];
			for (int j = 0; j < m; j++) {
				y[j]=(target[j]==i?1:0);
			}
			//����ѵ��
			cost+=lrps[i].trainBatch(input, y);
		}
		return cost;
	}
	/**
	 * one time test;
	 * @param input
	 * @return
	 */
	public Result test(double input[]){
		int result = 0;
		double output = lrps[result].test(input);
		for (int i = 1; i < classNum; i++) {
			double temp=lrps[i].test(input);
			if(output<temp){
				result =i;
				output = temp;
			}
		}
		if(output<=threshold){
			return new Result(-1, result, output);
		}
		return new Result(1, result, output);
	}
	/**
	 *  ������ȷ��,targetҪ��ȡֵΪ0~classNum-1;
	 * @param input
	 * @param target
	 * @return
	 */
	public double testBatch(double input[][] , int[] target){
		int m = input.length;
		int[] result = new int[m];
		double[] output = lrps[0].testBatch(input);
		for (int i = 1; i < classNum; i++) {
			double[] temp = lrps[i].testBatch(input);
			for (int j = 0; j < m; j++) {
				if(temp[j]>output[j]){
					output[j]=temp[j];
					result[j]=i;
				}
			}
		}
		int errNum=0;
		for (int i = 0; i <m; i++) {
			errNum+=(result[i]==target[i]?0:1);
		}
		return 1.0-errNum/(m+0.0);
	}
}
