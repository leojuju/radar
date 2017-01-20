package infrastruction.digitRecognize;

import java.io.Serializable;
import java.util.Random;

public class RegLogisticRegressionPerceptron implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	//Ȩ��
	private double[] weight;
	private int d;
//	private double[] prewalt;
	// ѧϰ��
	private double rate = 0.1;
	private double lambda=0.1;
	private int id;
	/**
	 * ������������ά�ȣ���ʼ��perceptron
	 * @param input_legnth
	 */
	public RegLogisticRegressionPerceptron(int input_length,int id){
		this.d=input_length;
		this.id=id;
		weight=new double[input_length+1];
		initiateWeight();
//		prewalt=new double[input_length+1];
	}
	
	public RegLogisticRegressionPerceptron(int input_length,double rate,int id){
		this(input_length,id);
		this.rate = rate;
	}
	
//	public LogisticRegressionPerceptron() {
//		super();
//	}

	/**
	 *  һ�ε�ѵ��,Ҫ�����targetȡֵΪ1��0;
	 * @param input
	 * @param y
	 */
	public void train(double[] input,int y){
		//����s=y(W*X)
		double sum=weight[0];//������
		for(int i=0;i<input.length;i++){
			sum=sum+input[i]*weight[i+1];
		}
		//�������out=sigmoid(-s);
		double output=1/(1+Math.exp(-sum));
		
		//�����ݶ�Gradient=(1-out)*(-y*W);�����ݶȽ�W����
		weight[0]+=-(y-output)*1;
		for (int i = 0; i < input.length; i++) {
			double delta= (y-output)*(-input[i]);
			weight[i+1]+=-delta*rate;
		}
	}
	/**
	 * һ���ε�ѵ��,Ҫ�����target����ȡֵΪ1��0
	 * @param input
	 * @param target
	 */
	public double trainBatch(double[][] input,int[] y){
		//ѵ��������
		int num = input.length;
		//feature��ά��
		int d = input[0].length;
		Double minCost =new Double(Double.POSITIVE_INFINITY);
		double newweight[] = new double[d+1];
		for (int i = 0; i < newweight.length; i++) {
			newweight[i]=weight[i];
		}
		while(true){
			// ����S=(W*X)
			// �������Out=sigmoid(-S);
			double cost=0;
			double[] sum = new double[num];
			double[] output = new double[num];
			for (int i = 0; i < num; i++) {
				sum[i] = newweight[0];
				for (int j = 0; j < d; j++) {
					sum[i] = sum[i] + input[i][j] *newweight[j+1];
				}
				output[i]=1 / (1 + Math.exp(-sum[i]));
				cost+=(-Math.log(output[i])*y[i]-Math.log(1-output[i])*(1-y[i]))/num;
			}
			cost+=lambda*squearW(newweight);
			System.out.println("No."+id+" cost:"+cost);
			if(cost>minCost||cost<0.001){
				break;
			}
			minCost=cost;
			for (int i = 0; i < weight.length; i++) {
				weight[i]=newweight[i];
			}
			// �����ݶ�Delta=-(y-out)*(X);
			double delta[]= new double[d+1];
			for (int i = 0; i < num; i++) {
				delta[0]+=-1*(y[i]-output[i])/num;
			}
			newweight[0]+=-delta[0]*rate;
			for (int j = 0; j < d; j++) {
				for (int i = 0; i < num; i++) {
					delta[j+1] +=-input[i][j]*(y[i]-output[i])/num;
				}
				newweight[j+1]+=-delta[j+1]*rate+lambda*newweight[j+1];
			}
		}
		return minCost;
	}
	
	private double squearW(double[] newweight) {
		double sum=0;
		for (int i = 1; i <newweight.length; i++) {
			sum+=newweight[i]*newweight[i];
		}
		return sum;
	}

	@SuppressWarnings("unused")
	private CostAndGradient costFunction(double[][] input,int[] y){
		// ѵ��������
		int num = input.length;
		// feature��ά��
		int d = input[0].length;
		// ����S=(W*X)
		// �������Out=sigmoid(-S);
		double[] sum = new double[num];
		double[] output = new double[num];
		double cost = 0;
		for (int i = 0; i < num; i++) {
			sum[i] = weight[0] ;
			for (int j = 0; j < d; j++) {
				sum[i] = sum[i] + input[i][j] * weight[j + 1];
			}
			output[i] = 1 / (1 + Math.exp(-sum[i]));
			cost +=( -y[i]*Math.log(output[i])-(1-y[i])*Math.log(1-output[i]) )/ num;
		}

		// �����ݶ�Delta=(1-out)*(-y*X);
		double gradient[] = new double[d+1];
		for (int i = 0; i < num; i++) {
			gradient[0] += (y[i] - output[i] * (-1) ) / num;
		}
		for (int j = 0; j < d; j++) {
			for (int i = 0; i < num; i++) {
				gradient[j+1] += (y[i]- output[i]) * (-input[i][j]) / num;
			}
		}
		return new CostAndGradient(cost, gradient);
	}
	
	
	/**
	 * one time test,����output����ֱ�۵õ����Ŷȡ�
	 * @param input
	 * @return
	 */
	public double test(double[] input){
		double sum=weight[0];
		for(int i=0;i<input.length;i++){
			sum=sum+input[i]*weight[i+1];
		}
		double output=1/(1+Math.exp(-sum));
		return output;
	}
	/**
	 * one batch test������output����ֱ�۵õ����Ŷȡ�
	 * @param input
	 * @return ȡֵ[1��-1]�Ľ�������顣
	 */
	public double[] testBatch(double[][] input){
		//ѵ��������
		int num = input.length;
		// feature��ά��
		int d = input[0].length;
		// ����S=(W*X)
		// �������Out=sigmoid(-S);
		double[] sum = new double[num];
		double[] output = new double[num];
		for (int i = 0; i < num; i++) {
			sum[i] = weight[0];
			for (int j = 0; j < d; j++) {
				sum[i] = sum[i] + input[i][j] * weight[j + 1];
			}
			output[i] = 1 / (1 + Math.exp(-sum[i]));
		}
		return output;
	}
	
	public void printWeight() {
		int add = 1;
		int num=0;
		for (int i = 0; i < weight.length; i++) {
			if(i>1){
				add*=2;
			}
			System.out.println(weight[i]);
			if(i>0&&i<weight.length-1){
				if(weight[i]<=0.5){
					num+=add;
				}
			}
		}
		System.out.println(-num+1);
	}
	
	private void initiateWeight(){
		Random ran = new Random();
		double e = Math.sqrt(6.0)/Math.sqrt(d+1);
		for (int i = 0; i < weight.length; i++) {
			weight[i]=ran.nextDouble()*2*e-e;
		}
	}
}
