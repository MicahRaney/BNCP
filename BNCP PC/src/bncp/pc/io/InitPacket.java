package bncp.pc.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InitPacket extends Packet {
	public int device, port;
	
	public InitPacket(){}
	public InitPacket(int device, int port){
		this.device = device;
		this.port = port;
	}
	
	@Override
	public void send(DataOutputStream out) throws IOException {
		out.write(Packet.getEncodedDevicePort(device, port));
	}

	@Override
	public void recieve(DataInputStream in) throws IOException {
		int devicePortCode = in.read();
		device = Packet.decodeDevice(devicePortCode);
		port = Packet.decodePort(devicePortCode);
	}
	
	@Override
	public int getDevicePortCode() {
		return Packet.getEncodedDevicePort(device, port);
	}

}
