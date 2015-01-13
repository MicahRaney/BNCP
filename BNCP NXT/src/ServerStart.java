import roboIO.PacketIO;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;


public class ServerStart {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args)  {
		while(true){
			USBConnectionServer server = new USBConnectionServer();
			server.init();
			server.start();
			System.out.println("main done...");
			Button.waitForAnyPress();
			Sound.buzz();
			server.interrupt();
			LCD.clear();
			System.out.println("Restarting...");
			Sound.beep();
		}
	}

}
