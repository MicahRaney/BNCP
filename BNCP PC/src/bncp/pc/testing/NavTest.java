package bncp.pc.testing;

import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.sensors.EmulatedSensor;
import bncp.pc.io.NXTPConnection;
import bncp.pc.io.Packet;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import bncp.pc.nav.PlaneNavigator;
import lejos.pc.comm.NXTCommException;

public class NavTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws NXTCommException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException, NXTCommException {
		NXTPConnection conn = new NXTPConnection();
		conn.init();
		conn.setDaemon(true);
		conn.start();

		JFrame frame = new JFrame("Geom Debug.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeometricPlane graph = new GeometricPlane(200,200,4);
		frame.add(graph);
		frame.pack();
		frame.setVisible(true);
		
		Scanner sysin = new Scanner(System.in);
		EmulatedSensor ult = conn.getEmulatedSensor(Packet.ULTRASONIC, Packet.PORT_4);
		EmulatedSensor comp = conn.getEmulatedSensor(Packet.HT_COMPASS, Packet.PORT_3);
		EmulatedSensor armrot = conn.getEmulatedSensor(Packet.MOTOR, Packet.PORT_A);
		EmulatedMotor arm = conn.getEmulatedMotor(Packet.PORT_A);
		conn.clearMotorTachometer(Packet.PORT_A);
		arm.rotate(145);
		conn.clearMotorTachometer(Packet.PORT_A);
		Thread.sleep(1000);
		arm.rotate(-90);
		
		PlaneNavigator nav = new PlaneNavigator(8,6);
		for(int i = 0; i < 100000; i++){
			int dist = ult.read();
			if(dist == 255){
				System.out.println("Nothing found...");
			}
			else if(dist > 100){
				putln("Ignoring...");
			}
			else{
				dist += 2;//compensate for sensor placement
				int c = comp.read(), a  = -1 *armrot.read();
				double[] pos = nav.calculatePos(comp.read(), -1*armrot.read(), dist);
				putln("graphed c:" + c+ " a:" + a  + " d:"+ dist);
				graph.addPoint(pos[0], pos[1]);
			}

			arm.rotate(10);
			if(armrot.read() > 90){
				putln("All done!");
				arm.rotate(-180);
				conn.rotateMotor(Packet.PORT_B, -120, false);
				conn.rotateMotor(Packet.PORT_C, 120, true);
				
			}
			Thread.sleep(50);
		}
	}

	public static void putln(String str){
		System.out.println(str);
	}
}
