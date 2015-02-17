package bncp.pc.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Blank packet that is sent to ping the connection. As soon as it is received,
 * it should be replied to. All the methods derived from Packet simply return
 * immediately, or return their default value.
 * 
 * @author micah
 *
 */
public class PingPacket extends Packet {

	public void send(DataOutputStream out) throws IOException {

	}

	public void recieve(DataInputStream in) throws IOException {

	}

	public int getDevicePortCode() {
		return 0;
	}
	
	public String toString(){
		return "PingPacket()";
	}

}
