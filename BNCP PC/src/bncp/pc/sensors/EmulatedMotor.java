package bncp.pc.sensors;

import java.io.IOException;

/**
 * Basic Interface for a motor that can be stopped, started or turned to a
 * specific rotation.
 * 
 * @author Micah
 *
 */
public interface EmulatedMotor {

	/**
	 * Rotate the motor for rotationDegrees degrees. If it is positive turn the
	 * motor forward or if rotationDegrees is negative turn the motor backwards.
	 * This method should block until the rotation is complete.
	 * 
	 * @param rotationDegrees
	 *            Number of degrees to turn the motor.
	 * @param block
	 *            If true, block the thread until the motor has rotated
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public void rotate(int rotationDegrees, boolean block) throws IOException,
			InterruptedException;

	/**
	 * Start the motor in the specified direction.
	 * 
	 * @param direction
	 *            True to rotate forward, false to rotate backward
	 * @throws IOException
	 */
	public void start(boolean direction) throws IOException;

	/**
	 * Stop the motor. Optimally block until the motor is stopped.
	 * 
	 * @throws IOException
	 *             If one occurs
	 * @throws InterruptedException
	 *             If one occurs
	 */
	public void stop() throws IOException, InterruptedException;

	/**
	 * Stop the motor and, if block is true, wait until the motor is stopped to
	 * return
	 * 
	 * @param block
	 *            If true block until the motor is stopped
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void stop(boolean block) throws IOException, InterruptedException;

	/**
	 * Gets the current rotation of the motor, in degrees.
	 * 
	 * @return Rotation of the motor in degrees.
	 * @throws IOException
	 *             If one occurs
	 * @throws InterruptedException
	 *             If one occurs.
	 */
	public int getDegrees() throws IOException, InterruptedException;

	/**
	 * Clears the count of degrees that the motor has turned.
	 * 
	 * @throws IOException
	 */
	public void clearTachometer() throws IOException;

	/**
	 * (from lejos.nxt.NXTRegulatedMotor javadoc) Sets desired motor speed , in
	 * degrees per second; The maximum reliably sustainable velocity is 100 x
	 * battery voltage under moderate load, such as a direct drive robot on the
	 * level.
	 * 
	 * @param speed
	 *            Speed of motor in degrees per second.
	 * @throws IOException
	 *             If an IOException Occurs
	 */
	public void setSpeed(int speed) throws IOException;

	/**
	 * (from lejos.nxt.NXTRegulatedMotor javadoc): sets the acceleration rate of
	 * this motor in degrees/sec/sec The default value is 6000; Smaller values
	 * will make speeding up. or stopping at the end of a rotate() task,
	 * smoother;
	 * 
	 * @param acceleration
	 *            Acceleration (in degrees*sec^-2) of the motor
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public void setAcceleration(int acceleration) throws IOException;

}
