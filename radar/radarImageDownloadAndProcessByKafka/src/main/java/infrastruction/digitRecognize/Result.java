package infrastruction.digitRecognize;

public class Result {
	public int status;
	public int result;
	public double rate;
	public Result(int status, int result, double rate) {
		super();
		this.status = status;
		this.result = result;
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "Result [status=" + status + ", result=" + result + ", rate=" + rate + "]";
	}
}
