package bncp.pc.io;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class Packet {

	public static int MOTOR = 0, TOUCH = 1, ULTRASONIC = 2, COLOR = 3,
			HT_COLOR = 4, HT_COMPASS = 5, LIGHT = 6;
	public static int PORT_1 = 1, PORT_2 = 2, PORT_3 = 3, PORT_4 = 4,
			PORT_A = 5, PORT_B = 6, PORT_C = 7;

	/**
	 * Send the packet over the specified DataOutputStream.
	 * 
	 * @param out
	 */
	public abstract void send(DataOutputStream out) throws IOException;

	/**
	 * Read the Packet from the given DataInputStream.
	 * 
	 * @param in
	 */
	public abstract void recieve(DataInputStream in) throws IOException;

	/**
	 * Returns the device and port, encoded as an integer (but actually a byte).
	 * Format: first 5 bits are the sensor id, last three are the port number.
	 */
	public static int getEncodedDevicePort(int device, int port) {
		return (device & 0x1f) << 3 | (port & 0x7);
	}

	/**
	 * Returns the port that is encoded in the devicePortCode. The port code is
	 * the last 3 bits of the 8 bit devicePortCode. (ie devicePortCode & 0x7).
	 * 
	 * @param devicePortCode
	 *            Code to decode
	 * @return Port denoted by the devicePortCode.
	 */
	public static int decodePort(int devicePortCode) {
		return devicePortCode & 0x7;
	}

	/**
	 * Returns the device that is decoded from the devicePortCode. The device
	 * code is the first 5 bits (out of 8 bits) of the devicePortCode (0xf9).
	 * 
	 * @param devicePortCode
	 *            Encoded device and port code.
	 * @return Device that is decoded from the devicePortCode.
	 */
	public static int decodeDevice(int devicePortCode) {
		return (devicePortCode & 0xF8) >> 3;// first 5 bits are the device code.
	}

	public abstract int getDevicePortCode();
}
