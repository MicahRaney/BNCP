package bncp.pc.sensors;

import java.io.IOException;

import bncp.pc.io.NXTPConnection;

public class NXTPSensor implements EmulatedSensor {

	NXTPConnection conn;
	int sensor, port;
	public NXTPSensor(NXTPConnection connection, int sensorID, int portID){
		conn = connection;
		sensor = sensorID;
		port = portID;
	}
	@Override
	public int read() throws IOException, InterruptedException {
		return conn.getSensorValue(sensor, port);
	}

}
