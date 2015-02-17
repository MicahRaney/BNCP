package bncp.pc.testing;

import lejos.pc.comm.NXTCommException;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BNCPConnectionFactory;
import bncp.pc.io.Packet;
import bncp.pc.sensors.EmulatedMotor;

public class MotorAccelerationExample {

	public static void main(String[] args) {
		try {

			System.out
					.println("Starting Motor Acceleration Example...\n"
							+ "Connect Motor A and B to the left and right motors...\n\n"
							+ "Press enter to Continue...");

			System.in.read();// block till a key is pressed.
			System.out.println("\n\nConnecting...");
			
			BNCPConnection conn = BNCPConnectionFactory.getUSBConnection();//get a USB Connection Instance.

			EmulatedMotor motorA = conn.getEmulatedMotor(Packet.PORT_A), motorB = conn
					.getEmulatedMotor(Packet.PORT_B);// get motor instances.

			motorA.setSpeed(1000);//set the speed high to make the acceleration effect
			motorB.setSpeed(1000);//more noticeable
			
			for (int accel = 6000; accel > 0; accel -= 500) {

				motorA.setAcceleration(accel);
				motorB.setAcceleration(accel);// set the acceleration

				System.out.println("Acceleration: " + accel
						+ " degrees*sec^-2");

				//rotate forward
				motorA.rotate(360, false);// don't block on Motor A because both
				// motors should run simultaneously
				motorB.rotate(360, true);// rotate and wait for motor B.

				//rotate back.
				motorA.rotate(-360, false);// don't block on Motor A because both
				// motors should run simultaneously
				motorB.rotate(-360, true);// rotate and wait for motor B.
				
				
				Thread.sleep(1000);//sleep for a bit.

			}

			System.out.println("\n\n\tDone!");
			
		} catch (Exception e) {
			System.err.println("Oops! Something happened!");
			e.printStackTrace();
		}

	}

}
