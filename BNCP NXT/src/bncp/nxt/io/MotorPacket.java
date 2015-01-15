package bncp.nxt.io;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MotorPacket extends Packet {

	public int port, value;
	public boolean forward, confirm;
	public static int CLEAR = -2, STOP_CODE = -1, INFINITE = 0;

	public MotorPacket() {
	};

	/**
	 * Initializes the packet to set the device at port to value.
	 * 
	 * @param port
	 *            Port that the device is at.
	 * @param value
	 *            Value to set the device to.
	 * @param direction
	 *            Direction to turn motor (if applicable) true means forward,
	 *            false means backward. 
	 *            
	 *           TODO: Remove DoConfirm param! This
	 *            should not be a part of the Packet class. Migrate it to the
	 *            PacketIO class
	 */
	public MotorPacket(int port, int value, boolean direction, boolean doConfirm) {
		this.port = port;
		this.value = value;
		forward = direction;
		confirm = doConfirm;
	}

	@Override
	public void send(DataOutputStream out) throws IOException {
		out.write(Packet.getEncodedDevicePort(Packet.MOTOR, port));
		out.writeInt(value);
		out.writeBoolean(forward);
		out.writeBoolean(confirm);
	}

	@Override
	public void recieve(DataInputStream in) throws IOException {
		int encodedDevice = in.read();
		port = Packet.decodePort(encodedDevice);
		value = in.readInt();
		forward = in.readBoolean();
		confirm = in.readBoolean();
	}

	@Override
	public int getDevicePortCode() {
		return Packet.getEncodedDevicePort(Packet.MOTOR, port);
	}

}
