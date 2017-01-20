package infrastruction.radarprocess.algebra;

/**
 * 表示三维直角坐标系中的一个点
 * @author linj
 *
 */
public class XYZ {
	double x;
	double y;
	double z;
	
	public XYZ(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector toVector(){
		return new Vector(x,y,z);
	}

	@Override
	public String toString() {
		return "XYZ [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
