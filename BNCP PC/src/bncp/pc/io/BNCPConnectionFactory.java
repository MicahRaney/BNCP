package bncp.pc.io;

import java.io.IOException;

import bncp.pc.wifi.client.WifiClient;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class BNCPConnectionFactory {

	/**
	 * Attempts to connect to a Mindstorms NXT Connected over USB.
	 * 
	 * @param args
	 * @throws NXTCommException
	 *             If one occurs, or if no NXT can be found.
	 */
	public static BNCPConnection getUSBConnection() throws NXTCommException {

		NXTComm conn = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo[] nxts = conn.search(null);
		if (nxts.length == 0) {
			System.out.println("No NXT Found!");
			throw new NXTCommException("No NXT Found!");
		}
		System.out.println("Connecting to the first NXT available...");
		conn.open(nxts[0], NXTComm.PACKET);

		System.out.println("Connected! Initializing IO...");
		PacketIO io = new BasicPacketIO(conn);
		System.out.println("IO Initialized!");

		BNCPConnection bncpconn = new BNCPConnection(io);
		bncpconn.start();
		return bncpconn;

	}

	/**
	 * Get a BNCPConnection for debugging. Whenever a packet is 'sent' it simply
	 * prints out the packet value. When a packet is 'read,' it returns a blank
	 * ReplyPacket.
	 * 
	 * @return BNCPConnection for debugging.
	 */
	public static BNCPConnection getDebugConnection() {

		BNCPConnection conn = new BNCPConnection(new PacketIODebugger());
		conn.start();
		return conn;
	}

	/**
	 * Initialize a connection to a WifiServer on the specified serverIP:port.
	 * @param serverIP IP of the remote server
	 * @param port Port to access server
	 * @return BNCPConnection that communicates over the Internet/LAN.
	 * @throws IOException If one occurs.
	 */
	public static BNCPConnection getWifiConnection(String serverIP, int port)
			throws IOException {
		WifiClient conn = new WifiClient(serverIP, port);
		// initialize a connection over LAN
		return conn.getConnection();
		// get the actual BNCPConnection and return it.
	}

}
