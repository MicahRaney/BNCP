package bncp.pc.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import lejos.pc.comm.NXTComm;

//import lejos.nxt.comm.NXTConnection;

public class BasicPacketIO implements PacketIO {
	/**
	 * String encoding used for all text based transfers.
	 */
	// public static String ENCODING="UTF-8";

	private static int MOTOR_PACKET = 0, GET_PACKET = 1, REPLY_PACKET = 2,
			INIT_PACKET = 3, SET_PACKET = 4, PING_PACKET = 5;
	DataInputStream in = null;
	DataOutputStream out = null;

	/**
	 * Initializes the PacketIO to use the NXTComm given.
	 * 
	 * @param conn
	 *            Connection to use.
	 */
	public BasicPacketIO(NXTComm conn) {
		this(new DataInputStream(conn.getInputStream()), new DataOutputStream(
				conn.getOutputStream()));
		// this(conn.openDataInputStream(), conn.openDataOutputStream());
	}

	/**
	 * Initializes the PacketIO to use the Socket given.
	 * 
	 * @param conn
	 *            Connection to use.
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public BasicPacketIO(Socket conn) throws IOException {
		this(new DataInputStream(conn.getInputStream()), new DataOutputStream(
				conn.getOutputStream()));
	}

	/**
	 * Initialize a connection with the specified connector. The connection must
	 * already be opened!
	 * 
	 * @param conn
	 */
	public BasicPacketIO(DataInputStream in, DataOutputStream out) {
		this.in = in;

		this.out = out;
	}

	/**
	 * Sends the given packet across the NXT Connection. Note: this method will
	 * wait until the whole packet is sent! This method flushes the
	 * DataOutputStream when done sending the Packet.
	 * 
	 * @param pkt
	 *            Packet to send.
	 */
	public void send(Packet pkt) throws IOException {

		synchronized (out) {//only send one packet at a time!

			out.write(getPacketID(pkt));// write the packet ID number.
			pkt.send(out);// send the packet.
			out.flush();
			
		}
	}

	/**
	 * Read a packet from the incoming input stream.
	 * 
	 * @return Packet recieved from connection.
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public Packet read() throws IOException {
		Packet pkt;
		synchronized (in) {// only read one packet at a time!
			
			int type = in.read();// what type of packet is it.
			pkt = getPacketType(type);
			pkt.recieve(in);// read the packet.
			
		}
		return pkt;
	}

	/**
	 * Returns the Integer ID of the packet. Used during packet transmission to
	 * tell the receiver what kind of packet is being sent, so the receiver can
	 * receive it appropriately.
	 * 
	 * At the time of writing, the packet IDs are: 1-MotorPacket 2-GetPacket
	 * 3-ReplyPacket 4-InitPacket 5-SetPacket
	 * 
	 * In order to add more packet types, you must edit the BNCP source code for
	 * both the NXT and the PC, otherwise the NXT will not be able to interpret
	 * the packets correctly.
	 * 
	 * @param pkt
	 *            Packet to determine ID of.
	 * @return Integer ID of Packet.
	 */
	public static int getPacketID(Packet pkt) {
		if (pkt instanceof MotorPacket)
			return MOTOR_PACKET;// return MotorPacket ID.
		else if (pkt instanceof GetPacket)
			return GET_PACKET;// GetPacket ID.
		else if (pkt instanceof ReplyPacket)
			return REPLY_PACKET;// ReplyPacket ID.
		else if (pkt instanceof InitPacket)
			return INIT_PACKET;// InitPacket ID
		else if (pkt instanceof SetPacket)
			return SET_PACKET;// SetPacket ID
		else if (pkt instanceof PingPacket)
			return PING_PACKET;// PingPacket ID
		else
			throw new IllegalArgumentException(
					"The packet type is not supported! Invalid Packet: "
							+ pkt.getClass());
	}

	/**
	 * Returns an instance of the subclass specified by ID. For example, if
	 * getPackeType(1) is called, the method will return an instance of
	 * MotorPacket.
	 * 
	 * At the time of writing, the packet IDs are: 1-MotorPacket 2-GetPacket
	 * 3-ReplyPacket 4-InitPacket 5-SetPacket
	 * 
	 * @param ID
	 *            ID of the Packet type.
	 * @return Instance of the subclass of Packet specified by the ID
	 */
	public static Packet getPacketType(int ID) {
		Packet pkt;
		if (ID == MOTOR_PACKET)
			pkt = new MotorPacket();
		else if (ID == GET_PACKET)
			pkt = new GetPacket();
		else if (ID == REPLY_PACKET)
			pkt = new ReplyPacket();
		else if (ID == INIT_PACKET)
			pkt = new InitPacket();
		else if (ID == SET_PACKET)
			pkt = new SetPacket();
		else if (ID == PING_PACKET)
			pkt = new PingPacket();
		else
			throw new IllegalArgumentException("Unrecognized Packet ID! ID="
					+ ID);
		return pkt;
	}

}
