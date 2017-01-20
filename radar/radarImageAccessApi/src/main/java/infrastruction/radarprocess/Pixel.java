package infrastruction.radarprocess;

public class Pixel {
	public int x;
	public int y;
	public Pixel(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public String toSimpleString() {
		return "[" + x + "," + y + "]";
	}
}
