package bncp.nxt.comm;

import java.io.IOException;

import bncp.nxt.io.Packet;
import bncp.nxt.io.PacketIO;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * This thread constantly checks for input on the USB connection, and when input
 * arrives, forwards the packet to the PacketHandler specified in the
 * constructor.
 * 
 * @author micah
 *
 */
public class USBConnectionServer extends Thread {

	PacketIO io = null;
	private PacketHandler handler = null;

	/**
	 * Set up the USBConnectionServer to handle the incoming packets with the
	 * specified PacketHandler.
	 * 
	 * @param handler
	 *            Handler to use to handle incoming packets.
	 */
	public USBConnectionServer(PacketHandler handler) {
		this.handler = handler;
	}

	/**
	 * This will initialize the USB connection, and will block until an
	 * exception occurs or a connection is found.
	 */
	public void init() {

		USBConnection conn;
		System.out.println("Waiting for connection");
		do {

			conn = USB.waitForConnection();

		} while (conn == null);
		System.out.println("Connection Found!");
		io = new PacketIO(conn);// initialize connection.

		System.out.println("IO Initialized");
	}

	/**
	 * Start the USBConnectionServer. Basic run cycle is to read a packet, then
	 * handle the packet until an error occurs or .interrupt() is called.
	 * 
	 * NOTE: Don't directly call run unless you want the calling thread to not
	 * return for a long time! To start a new thread, call .start()!
	 */
	@Override
	public void run() {
		if (io == null)// if the thread isn't initialized, initialize it!
			init();
		System.out.println("Server starting...");
		try {
			int c = 0;
			while (!isInterrupted()) {
				Packet pkt = io.read();// read a packet.
				System.out.println("Got pkt " + c);
				Packet replyPkt = handler.handlePacket(pkt);
				if (replyPkt != null)
					io.send(replyPkt);// if there is a reply to be sent, send
										// it!
				c++;// increment packet counter.
			}
			System.out.println("Shutting down...");
		} catch (IOException e) {
			Motor.A.stop(true);
			Motor.B.stop(true);// stop all the motors! Avoid continuing on when
			Motor.C.stop(true);// the connection is broken.
			System.out.println("IOException!");
			e.printStackTrace();
			Button.waitForAnyPress();
		}
	}
}
