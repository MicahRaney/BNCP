package bncp.pc.wifi.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BasicPacketIO;
import bncp.pc.io.Packet;
import bncp.pc.io.PacketIO;
import bncp.pc.io.PingPacket;
import bncp.pc.wifi.Authentication;
import bncp.pc.wifi.Common;

public class WifiServer extends Thread {

	BNCPConnection nxtconn;
	private LinkedList<PingListener> pingListeners = new LinkedList<PingListener>();

	public WifiServer(BNCPConnection conn) {
		nxtconn = conn;
	}

	public void run() {

		while (!isInterrupted()) {

			try {

				// get connection.
				ServerSocket ssock = new ServerSocket(Common.PORT);
				Socket sock = ssock.accept();
				ssock.close();
				PacketIO io = new BasicPacketIO(sock);

				// TODO: Authentication/Handshake.
				if (!Authentication.Authenticate(io)) {
					// TODO: AUTH FAILED! Do what?
					System.out.println("Authentication failed...");
					sock.close();
					return;
				}

				while (!isInterrupted()) {
					Packet in = io.read();
					if (in instanceof PingPacket) {
						long time = System.currentTimeMillis();
						for (PingListener listener : pingListeners)
							listener.pingRecieved(time);
					} else
						nxtconn.send(in);
				}

			} catch (Exception e) {

				e.printStackTrace();
			
			}
		}
	}

	public void addPingListener(PingListener pl) {
		pingListeners.add(pl);
	}

	public void suggestStop() {
		interrupt();
	}

}
