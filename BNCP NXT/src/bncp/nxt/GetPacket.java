package bncp.nxt;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class GetPacket extends Packet {

	public int device, port;
	
	public GetPacket(){};
	/**
	 * Makes a new GetPacket for the specified device and port
	 * @param device For device codes see Packet class
	 * @param port For port codes see Packet class
	 */
	public GetPacket(int device, int port){
		this.device = device;
		this.port = port;
	}
	@Override
	public void send(DataOutputStream out) throws IOException {
		out.write(Packet.getEncodedDevicePort(device, port));
		
	}

	@Override
	public void recieve(DataInputStream in) throws IOException {
		int encodedInfo = in.read();
		device = Packet.decodeDevice(encodedInfo);
		port = Packet.decodePort(encodedInfo);
	}
	@Override
	public int getDevicePortCode() {
		return Packet.getEncodedDevicePort(device, port);
	}

}
