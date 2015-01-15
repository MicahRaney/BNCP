package bncp.pc.sensors;

import java.io.IOException;

import bncp.pc.io.GetPacket;
import bncp.pc.io.MotorPacket;
import bncp.pc.io.NXTPConnection;
import bncp.pc.io.Packet;


public class NXTPMotor implements EmulatedMotor{

	private final NXTPConnection conn;
	private final int port;
	
	public NXTPMotor(NXTPConnection connection, int port){
		conn = connection;
		this.port = port;
	}
	
	@Override
	public void rotate(int rotation, boolean block) throws IOException, InterruptedException {
		boolean forward = true;
		if (rotation < 0) {
			forward = false;
			rotation *= -1;
		}
		Packet pkt = new MotorPacket(port, rotation, forward, block);
		if (block)
			conn.waitForReply(pkt, Packet.MOTOR, port);
		else
			conn.send(pkt);
	}

	@Override
	public void start(boolean direction) throws IOException{
		conn.send(new MotorPacket(port, 0, direction, false));
	}

	@Override
	public void stop() throws IOException, InterruptedException{
		stop(true);
	}

	@Override
	public int getDegrees() throws IOException, InterruptedException {
		return conn.waitForReply(new GetPacket(Packet.MOTOR, port), Packet.MOTOR, port).value;
	}
 
	@Override
	public void clearTachometer() throws IOException {
		conn.send(new MotorPacket(port, MotorPacket.CLEAR, false, true));
	}

	@Override
	public void stop(boolean block) throws IOException, InterruptedException {
		Packet pkt = new MotorPacket(port, -1, false, block);
		if (block)
			conn.waitForReply(pkt, Packet.MOTOR, port);
		else
			conn.send(pkt);
	}


}
