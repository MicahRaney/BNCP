package bncp.nxt;

import bncp.nxt.comm.BasicPacketHandler;
import bncp.nxt.comm.USBConnectionServer;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class ServerStart {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		while (true) {
			USBConnectionServer server = new USBConnectionServer(
					new BasicPacketHandler());
			server.init();
			server.start();
			Button.waitForAnyPress();
			Sound.buzz();
			server.interrupt();
			LCD.clear();
			System.out.println("Resetting...");
			lejos.nxt.Motor.A.stop();// stop all motors if user initiates manual
			lejos.nxt.Motor.B.stop();//reset
			lejos.nxt.Motor.C.stop();
			System.out.println("Restarting...");
			Sound.beep();
		}
	}

}
