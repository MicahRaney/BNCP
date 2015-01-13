package roboIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//import lejos.pc.comm.NXTComm;
import lejos.nxt.comm.NXTConnection;


public class PacketIO {
	/**
	 * String encoding used for all text based transfers.
	 */
	//public static String ENCODING="UTF-8";	
	private static int MOTOR_PACKET = 0, GET_PACKET = 1, REPLY_PACKET = 2, INIT_PACKET = 3, SET_PACKET = 4;
	DataInputStream in = null;
	DataOutputStream out = null;

	/**
	 * Initializes the PacketIO to use the NXTComm given.
	 * @param conn Connection to use.
	 */
	public PacketIO(NXTConnection conn){
		//this(new DataInputStream(conn.getInputStream()), new DataOutputStream(conn.getOutputStream()));
		this(conn.openDataInputStream(), conn.openDataOutputStream());
	}

	/**
	 * Initialize a connection with the specified connector. The connection must already be opened!
	 * @param conn
	 */
	public PacketIO(DataInputStream in, DataOutputStream out){
		this.in = in;
		
		this.out = out;
	}
	
	/**
	 * Sends the given packet across the NXT Connection. Note: this method will wait until
	 * the whole packet is sent! This method flushes the DataOutputStream when done sending the
	 * Packet.
	 * @param pkt Packet to send.
	 */
	public void send(Packet pkt) throws IOException{

		if(pkt instanceof MovePacket)
			out.write(MOTOR_PACKET);
		else if(pkt instanceof GetPacket)
			out.write(GET_PACKET);
		else if(pkt instanceof ReplyPacket)
			out.write(REPLY_PACKET);
		else if(pkt instanceof InitPacket)
			out.write(INIT_PACKET);
		else if(pkt instanceof SetPacket)
			out.write(SET_PACKET);
		else
			throw new IllegalArgumentException("The packet type is not supported! Modify the code to recognize it!");
		

		pkt.send(out);//send the packet.
		out.flush();
	}

	/**
	 * Read a packet from the incoming input stream.
	 * @return Packet recieved from connection.
	 * @throws IOException If an IOException occurs.
	 */
	public synchronized Packet read() throws IOException{
		int type = in.read();//what type of packet is it.
		Packet pkt;
		if (type == MOTOR_PACKET)
			pkt = new MovePacket();
		else if (type == GET_PACKET)
			pkt = new GetPacket();
		else if (type == REPLY_PACKET)
			pkt = new ReplyPacket();
		else if (type == INIT_PACKET)
			pkt = new InitPacket();
		else if (type == SET_PACKET)
			pkt = new SetPacket();
		else
			throw new IOException("Unrecognized Packet Type! type=" + type);
		pkt.recieve(in);//read the packet.
		return pkt;
	}
}
