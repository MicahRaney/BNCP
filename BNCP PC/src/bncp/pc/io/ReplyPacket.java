package bncp.pc.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReplyPacket extends Packet {

	public int device, port, value;
		
	public ReplyPacket(){};
	
	public ReplyPacket(int device, int port, int value){
		this.device = device;
		this.port = port;
		this.value = value;
	}
	
	@Override
	public void send(DataOutputStream out) throws IOException {
		out.write(Packet.getEncodedDevicePort(device, port));
		out.writeInt(value);
	}

	@Override
	public void recieve(DataInputStream in) throws IOException {
		int devicePort = in.read();
		device = Packet.decodeDevice(devicePort);
		port = Packet.decodePort(devicePort);
		value = in.readInt();
	}
	
	@Override
	public int getDevicePortCode() {
		return Packet.getEncodedDevicePort(device, port);
	}

}
