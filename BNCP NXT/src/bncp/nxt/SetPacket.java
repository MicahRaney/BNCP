package bncp.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SetPacket extends Packet {
	public int device, port, type, val;
	
	public static final int MOTOR_SPEED = 0, MOTOR_ACCEL = 1;
	
	/**
	 * Blank constructor for SetPacket. Note: this will leave all variables as null, so do not attempt to
	 * read from the SetPacket until calling .read(DataInpuStream) or manually initializing the variables.
	 */
	public SetPacket(){};
	
	/**
	 * Basic packet to set a value on the NXT. Currently supported in the NXTP protocol you can
	 * set motor speed and motor acceleration.
	 * 
	 * @param device Device to set attribute of
	 * 
	 * @param port port of device given
	 * @param type type of attribute to set (member variable of SetPacket)
	 * @param value Value to set.
	 */
	public SetPacket(int device, int port, int type, int value){
		this.device = device;
		this.port = port;
		this.type = type;
		val = value;
	}
	
	@Override
	public void send(DataOutputStream out) throws IOException {
		out.write(Packet.getEncodedDevicePort(device, port));
		out.write(type);
		out.writeInt(val);
	}

	@Override
	public void recieve(DataInputStream in) throws IOException {
		int encoded = in.read();
		device = Packet.decodeDevice(encoded);
		port = Packet.decodePort(encoded);//decode device and port
		type = in.read();
		val = in.readInt();
	}

	@Override
	public int getDevicePortCode() {
		return Packet.getEncodedDevicePort(device, port);
	}


}
