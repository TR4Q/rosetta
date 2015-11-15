import java.util.ArrayList;

public class Functions{
	public static double GConstant = 2.58347673E-37;

	//vector operations: multiplying and dividing by a scalar
	public static double[] scalarTimesVector(double scalar, double[] vector){
		double xVec = scalar*vector[0];
		double yVec = scalar*vector[1];
		double zVec = scalar*vector[2]; 
		double[] scalaredVector = {xVec,yVec,zVec};
		return scalaredVector;
	}

	//vector operations, adds vectors inside the arraylist
	public static double[] sumVectors(ArrayList<double[]> vectors){
		double xTotal = 0;
		double yTotal = 0;
		double zTotal = 0;
		for(double[] each:vectors){
			xTotal+=each[0];
			yTotal+=each[1];
			zTotal+=each[2];
		}
		double[] summedVector = {xTotal,yTotal,zTotal};
		return summedVector;
	}

	//finds some constant g times m times r divided by r cubed
	public static double findGMRCubed(double mass, double[] radius){
		double mag = Math.sqrt(radius[0]*radius[0]+radius[1]*radius[1]+radius[2]*radius[2]);
		return GConstant*mass/mag/mag/mag;
	}

	//takes in and return arrays of size 3: x,y,z
	public static double[] findDistanceBetween(double[] rosetta, double[] other){
		double xDiff = -other[0]+rosetta[0];
		double yDiff = -other[1]+rosetta[1];
		double zDiff = -other[2]+rosetta[2];
		double[] arrayDiff = {xDiff,yDiff,zDiff};
		return arrayDiff;
	}

	static final double scalingConstantMouse = 5.7*Math.pow(10,-7);
	static double scalingConstant = 5.7*Math.pow(10,-7);
	public static int AUScaler(double AU){
		return (int) (AU*149597871*scalingConstant);
	}
}