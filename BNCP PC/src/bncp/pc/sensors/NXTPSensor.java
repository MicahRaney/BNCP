package bncp.pc.sensors;

import java.io.IOException;

import bncp.pc.io.BNCPConnection;

public class NXTPSensor implements EmulatedSensor {

	BNCPConnection conn;
	int sensor, port;
	public NXTPSensor(BNCPConnection connection, int sensorID, int portID){
		conn = connection;
		sensor = sensorID;
		port = portID;
	}
	@Override
	public int read() throws IOException, InterruptedException {
		return conn.getSensorValue(sensor, port);
	}

}
