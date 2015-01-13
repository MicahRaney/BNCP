package roboIO.nav;

public class PlaneNavigator {

	private double[] offset = new double[2];
	public static int DISTANCE_POS = 0, ANGLE_POS = 1;
	public PlaneNavigator(int xSensorOffset, int ySensorOffset) {
		calculateOffset(xSensorOffset,ySensorOffset);
	}
	
	
	public void calculateOffset(double xSensorOffset, double ySensorOffset){
		offset[DISTANCE_POS] = Math.sqrt((xSensorOffset*xSensorOffset)+(ySensorOffset*ySensorOffset));
		offset[ANGLE_POS] = deg(Math.atan(xSensorOffset/ySensorOffset));
		System.out.println("Calculated " + offset[DISTANCE_POS] + " at " + offset[ANGLE_POS]);
	}
	
	public double[] calculatePos(double heading, double armHeading, double distance){
		double x = Math.sin(rad(heading + armHeading)) * distance;//calculate the initial x component
		x += Math.sin(rad(heading + offset[ANGLE_POS])) * offset[DISTANCE_POS];//factor in the sensor offset
		double y = Math.cos(rad(heading + armHeading)) * distance;//calculate the initial x component
		y += Math.cos(rad(heading + offset[ANGLE_POS])) * offset[DISTANCE_POS];//factor in the sensor offset
		
		return new double[]{x,y} ;
	}
	private double rad(double deg){
		return Math.toRadians(deg);
	}
	private double deg(double rad){
		return Math.toDegrees(rad);
	}
}
