package bncp.nxt.comm;

import java.io.IOException;

import bncp.nxt.io.Packet;

/**
 * Interface for Objects that can handle and interpret the Packets defined in
 * the bncp.nxt.io package.
 * 
 * @author Micah Raney
 *
 *
 */
public interface PacketHandler {

	/**
	 * Handle the packet by interpreting it and performing the corresponding
	 * action, and if applicable provide a reply packet. A return value of NULL
	 * should mean that there is no reply to send back as a response.
	 * 
	 * @param pkt Packet to interpret
	 */
	public Packet handlePacket(Packet pkt) throws IOException;

}
