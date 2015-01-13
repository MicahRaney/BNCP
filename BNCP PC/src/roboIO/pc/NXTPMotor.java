package roboIO.pc;

import java.io.IOException;

import roboIO.Packet;


public class NXTPMotor implements EmulatedMotor{

	private final NXTPConnection conn;
	private final int port;
	
	public NXTPMotor(NXTPConnection connection, int port){
		conn = connection;
		this.port = port;
	}
	
	@Override
	public void rotate(int rotationDegrees) throws IOException, InterruptedException {
		conn.rotateMotor(port, rotationDegrees, true);
	}

	@Override
	public void start(boolean direction) throws IOException{
		conn.startMotor(port, direction);
	}

	@Override
	public void stop() throws IOException, InterruptedException{
		stop(true);
	}

	@Override
	public int getDegrees() throws IOException, InterruptedException {
		return conn.getSensorValue(Packet.MOTOR, port);
	}

	@Override
	public void clearCounter() throws IOException {
		conn.clearMotorTachometer(port);//clear the tacho count.
	}

	@Override
	public void stop(boolean block) throws IOException, InterruptedException {
		conn.stopMotor(port, block);
	}

}
