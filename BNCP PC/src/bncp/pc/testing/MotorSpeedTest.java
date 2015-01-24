package bncp.pc.testing;

import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.sensors.EmulatedSensor;
import bncp.pc.io.NXTPConnection;
import bncp.pc.io.Packet;

import java.io.IOException;
import java.util.Scanner;

import lejos.pc.comm.NXTCommException;

public class MotorSpeedTest {

	/**
	 * Example program of changing motor speed using the BNCP PC libraries.
	 * Motor A is increased in acceleration until nMaxSpeed is reached, and the
	 * acceleration will be decreased back to one, whereupon the speed will
	 * increase again.
	 * 
	 * @author Micah Raney
	 * 
	 * @param args
	 * @throws IOException
	 * @throws NXTCommException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			NXTCommException, InterruptedException {
		NXTPConnection conn = new NXTPConnection();
		conn.init();
		conn.setDaemon(true);
		conn.start();
		EmulatedMotor motorA = conn.getEmulatedMotor(Packet.PORT_A);

		Scanner in = new Scanner(System.in);
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
