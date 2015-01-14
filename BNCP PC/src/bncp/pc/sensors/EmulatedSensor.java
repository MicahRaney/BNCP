package bncp.pc.sensors;

import java.io.IOException;

public interface EmulatedSensor {
	
	public int read() throws IOException, InterruptedException;

}
