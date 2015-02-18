package bncp.pc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import lejos.pc.comm.NXTCommException;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BNCPConnectionFactory;
import bncp.pc.io.Packet;
import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.testing.MovementControlPanel;
import bncp.pc.testing.pinch.gui.MotorControlPanel;
import bncp.pc.wifi.Common;
import bncp.pc.wifi.server.PingDaemon;
import bncp.pc.wifi.server.WifiServer;

public class Main {

	public static void main(String[] args) throws NXTCommException, IOException {
		Scanner in = new Scanner(System.in);
		BNCPConnection conn;
		PingDaemon pd;
		WifiServer server;
		System.out.print("Server | client | debug-client: ");
		switch (in.nextLine().toLowerCase()) {

		case "server":

			conn = BNCPConnectionFactory.getUSBConnection();
			server = new WifiServer(conn);

			pd = new PingDaemon(conn, Common.PING_ALARM);
			server.addPingListener(pd);
			server.start();
			pd.start();

			break;

		case "client":
			System.out.print("IP: ");

			conn = BNCPConnectionFactory.getWifiConnection(in.nextLine(),
					Common.PORT);// initialize a connection over WiFi/LAN

			startClient(conn);

			break;
		case "debug-client":
			startClient(BNCPConnectionFactory.getDebugConnection());
			break;
		default:
			System.out.println("Please enter server | client | debug-client!");

		}
		in.close();

	}

	public static void startClient(BNCPConnection conn) throws IOException {
		EmulatedMotor a = conn.getEmulatedMotor(Packet.PORT_A), b = conn
				.getEmulatedMotor(Packet.PORT_B);

		a.setAcceleration(2000);
		b.setAcceleration(2000);

		MotorControlPanel mcp = new MotorControlPanel();
		mcp.addMotor(a);
		mcp.addMotor(b);
		
		JFrame window = new JFrame("Movement Control Debugger");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setLayout(new BorderLayout());
		window.add(new MovementControlPanel(a, b), BorderLayout.CENTER);
		window.add(mcp, BorderLayout.WEST);
		
		window.setSize(new Dimension(150, 100));
		window.pack();
		window.setVisible(true);
		
	}

}
