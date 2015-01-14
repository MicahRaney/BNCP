package bncp.nxt;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.addon.CompassHTSensor;
public class SensorWrapper {

	private Object sensor;
	
	public SensorWrapper(Object sensor){
		this.sensor = sensor;
	}
	
	public int read(){
		if(sensor instanceof UltrasonicSensor)
			return ((UltrasonicSensor)sensor).getDistance();
		if(sensor instanceof NXTRegulatedMotor)
			return ((NXTRegulatedMotor)sensor).getTachoCount();
		if(sensor instanceof TouchSensor)
			return Integer.parseInt(Boolean.toString(((TouchSensor)sensor).isPressed()));
		if(sensor instanceof CompassHTSensor)
			return (int)((CompassHTSensor)sensor).getDegrees();
		if(sensor instanceof ColorHTSensor)
				return ((ColorHTSensor)sensor).getColorID();
		else
			throw new IllegalStateException("Invalid SensorWrapper!");
	}
	
}
