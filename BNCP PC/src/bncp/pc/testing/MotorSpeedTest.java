package bncp.pc.testing;

import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BNCPConnectionFactory;
import bncp.pc.io.Packet;

import java.io.IOException;
import lejos.pc.comm.NXTCommException;


/**
 * Example program of changing motor speed using the BNCP PC libraries.
 * Motor A is increased in acceleration until nMaxSpeed is reached, and the
 * acceleration will be decreased back to one, whereupon the speed will
 * increase again.
 * 
 * @author Micah Raney
 * 
 */
public class MotorSpeedTest {

	/**
	 * Main method connects to the NXT and tests motor speed.
	 * 
	 * @param args Program arguments (ignored)
	 * @throws IOException If IO error occurs. 
	 * @throws NXTCommException If communication error occurs.
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			NXTCommException, InterruptedException {

		BNCPConnection conn = BNCPConnectionFactory.getUSBConnection();

		EmulatedMotor motorA = conn.getEmulatedMotor(Packet.PORT_A);

		motorA.start(true);

		int nMaxSpeed = 1000, nSpeed = 0, nSleep = 5;
		boolean bIteratingUp = true;
		while (true) {
			if (bIteratingUp)
				nSpeed++;
			else
				nSpeed--;

			if (nSpeed >= nMaxSpeed)
				bIteratingUp = false;// start iterating down
			if (nSpeed <= 1)
				bIteratingUp = true; // start iterating up

			motorA.setSpeed(nSpeed);// apply the speed.
			Thread.sleep(nSleep);

		}

	}

}
