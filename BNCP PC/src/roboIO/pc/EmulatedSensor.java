package roboIO.pc;

import java.io.IOException;

public interface EmulatedSensor {
	
	public int read() throws IOException, InterruptedException;

}
