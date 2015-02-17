package bncp.pc.io;

import java.io.IOException;

public interface PacketIO {

	/**
	 * Send the packet over the PacketIO connection.
	 * 
	 * @param pkt
	 *            Packet to send
	 */
	public void send(Packet pkt) throws IOException;

	/**
	 * Read a packet over the PacketIO and return it.
	 * 
	 * @return Packet read
	 */
	public Packet read() throws IOException;

}
