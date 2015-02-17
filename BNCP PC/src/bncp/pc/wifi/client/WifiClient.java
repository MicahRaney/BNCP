package bncp.pc.wifi.client;

import java.io.IOException;
import java.net.Socket;

import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BasicPacketIO;
import bncp.pc.io.PacketIO;
import bncp.pc.io.PingPacket;
import bncp.pc.wifi.Authentication;
import bncp.pc.wifi.Common;

public class WifiClient extends Thread {

	private BNCPConnection conn;

	public WifiClient(String ip, int port) throws IOException {

		System.out.println("Starting BNCP WiFi Client...");
		Socket sock = new Socket(ip, port);

		PacketIO io = new BasicPacketIO(sock);

		if (Authentication.Authenticate(io)) {
			conn = new BNCPConnection(io);
			conn.start();

		} else {
			System.out.println("Authentication failed!");
		}

		start();// start the auto-pinger.

	}

	public BNCPConnection getConnection() {
		return conn;
	}

	public void run() {

		try {
			while (!isInterrupted()) {
				Thread.sleep(Common.PING_TIME);
				conn.send(new PingPacket());

			}
		} catch (InterruptedException | IOException e) {
			System.out.println("WifiClient Ping thread exception!");
			e.printStackTrace();
		}

	}

}
