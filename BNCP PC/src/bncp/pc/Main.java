package bncp.pc;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import lejos.pc.comm.NXTCommException;
import bncp.pc.io.NXTPConnection;
import bncp.pc.io.Packet;
import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.testing.MovementControlPanel;
import bncp.pc.wifi.Common;
import bncp.pc.wifi.client.WifiClient;
import bncp.pc.wifi.server.PingDaemon;
import bncp.pc.wifi.server.WifiServer;

public class Main {

	public static void main(String[] args) throws NXTCommException, IOException {
		Scanner in = new Scanner(System.in);
		NXTPConnection conn;
		System.out.print("Server | client: ");
		switch(in.nextLine().toLowerCase()){
		
		case "server":

			conn = new NXTPConnection();
			conn.init();
			WifiServer server = new WifiServer(conn);

			PingDaemon pd = new PingDaemon(conn, Common.PING_ALARM);
			server.addPingListener(pd);
			server.start();
			pd.start();
			
			break;
			
		case "client":
			System.out.print("IP: ");
			
			WifiClient wicli = new WifiClient(in.nextLine(),Common.PORT);
			

			conn = wicli.getConnection();
			
			EmulatedMotor a = conn.getEmulatedMotor(Packet.PORT_A), b = conn.getEmulatedMotor(Packet.PORT_B);

			a.setAcceleration(2000);
			b.setAcceleration(2000);

			JFrame window = new JFrame("Movement Control Debugger");
			window.add(new MovementControlPanel(a,b));
			window.setSize(new Dimension(150,100));
			window.setVisible(true);
		
			
			break;
		
		default:
			System.out.println("Please enter server | client!");
		
		}

	}

}
