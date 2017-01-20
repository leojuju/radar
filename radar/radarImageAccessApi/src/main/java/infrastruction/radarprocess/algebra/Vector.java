package infrastruction.radarprocess.algebra;

/**
 * 表示一个三维向量
 * @author linj
 *
 */
public class Vector {
	public double x;
	public double y;
	public double z;
	
	public Vector(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(XYZ startPoint,XYZ endPoint) {
		super();
		this.x = endPoint.x-startPoint.x;
		this.y = endPoint.y-startPoint.y;
		this.z = endPoint.z-startPoint.z;
	}

	public Vector(Vector startVector,Vector endVector) {
		super();
		this.x = endVector.x-startVector.x;
		this.y = endVector.y-startVector.y;
		this.z = endVector.z-startVector.z;
	}
	
	
	public XYZ toXYZ(){
		return new XYZ(x,y,z);
	}
	
	public Vector add(Vector vector){
		if(vector==null){
			return this;
		}
		this.x+=vector.x;
		this.y+=vector.y;
		this.z+=vector.z;
		return this;
	}
	
	public Vector mul(double d){
		this.x*=d;
		this.y*=d;
		this.z*=d;
		return this;
	}

	public boolean isZero() {
		return (x*x+y*y+z*z)==0;
	}

	public double mod() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	
	public double vmul(Vector v){
		return this.x*v.x+this.y*v.y+this.z*v.z;
	}
	
	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
