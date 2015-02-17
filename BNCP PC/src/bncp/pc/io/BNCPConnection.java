package bncp.pc.io;

import java.io.IOException;
import java.util.LinkedList;
import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.sensors.EmulatedSensor;
import bncp.pc.sensors.NXTPMotor;
import bncp.pc.sensors.NXTPSensor;

/**
 * @author Micah Raney
 *
 */
public class BNCPConnection extends Thread {

	private volatile LinkedList<PendingHolder> pending = new LinkedList<PendingHolder>();

	private PacketIO io;

	public BNCPConnection() {
		setDaemon(true);
	}

	/**
	 * Create an NXTPConnection using the PacketIO given.
	 * 
	 * @param io
	 *            PacketIO to do communication with.
	 */
	public BNCPConnection(PacketIO io) {
		this.io = io;
		setDaemon(true);
	}

	@Override
	public void run() {

		if (io == null)
			throw new IllegalStateException(
					"NXTPConnection not initialized! Cannot continue!");

		try {

			while (!this.isInterrupted()) {
				Packet pkt = read();
				if (pkt instanceof ReplyPacket) {// is there a thread waiting
													// for
													// this packet?
					synchronized (pending) {
						int id = pkt.getDevicePortCode();
						for (PendingHolder hld : pending) {// find the
															// PendingHolder
															// that is waiting
							if (hld.ID == id) {
								synchronized (hld) {
									hld.reply = (ReplyPacket) pkt;
									hld.notifyAll();// notify all the threads
													// waiting on the
													// PendingHolder.
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Something happened in NXTPConnection!");
			e.printStackTrace();
		}

	}

	/**
	 * Sends a packet over the connection.
	 * 
	 * @param pkt
	 *            Packet to send.
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public synchronized void send(Packet pkt) throws IOException {
		io.send(pkt);
	}

	/**
	 * Reads a packet from the connection.
	 * 
	 * @return Packet
	 * @throws IOException
	 *             if one occurs.
	 */
	private Packet read() throws IOException {
		return io.read();
	}

	public void initSensor(int sensor, int port) throws IOException {
		send(new InitPacket(sensor, port));
	}

	public int getSensorValue(int sensor, int port) throws IOException,
			InterruptedException {

		return waitForReply(new GetPacket(sensor, port), sensor, port).value;

		// throw new
		// IllegalArgumentException("This method can't be called yet :|... Not implemented!");
		/*
		 * Integer packetId = OldPacket.getEncodedDevicePort(sensor, port);
		 * pending.add(packetId); //send(new OldPacket(OldPacket.GET_PACKET,
		 * sensor, port, 0 , 0)); packetId.wait();
		 */
	}

	public void clearMotorTachometer(int port) throws IOException {
		send(new MotorPacket(port, MotorPacket.CLEAR, false, true));
	}

	public EmulatedSensor getEmulatedSensor(int device, int port)
			throws IOException {
		initSensor(device, port);
		return new NXTPSensor(this, device, port);
	}

	public EmulatedMotor getEmulatedMotor(int port) {
		return new NXTPMotor(this, port);
	}

	public ReplyPacket waitForReply(Packet toSend, int sensor, int port)
			throws IOException, InterruptedException {

		if (!isAlive())// if the thread has stopped/hasn't started, throw
						// an
			// exception.
			throw new IllegalStateException(
					"The NXTPConnection thread is not running! Cannot wait for a reply packet!");

		PendingHolder hld = new PendingHolder(Packet.getEncodedDevicePort(
				sensor, port));
		synchronized (pending) {
			pending.add(hld);
			send(toSend);
		}

		synchronized (hld) {
			hld.wait();
		}
		return hld.reply;

	}

	/**
	 * Attempts to close the connection with the NXT
	 * 
	 * @throws IOException
	 *             If an IOException occurs
	 */
	public void close() throws IOException {
		throw new IllegalStateException("Method not implemented!");
	}

	public static class PendingHolder {
		public int ID;
		public ReplyPacket reply;

		public PendingHolder(int id) {
			this.ID = id;
		}

		@Override
		public boolean equals(Object obj) {
			if (((PendingHolder) obj).ID == ID)
				return true;
			else
				return false;
		}
	}
}
