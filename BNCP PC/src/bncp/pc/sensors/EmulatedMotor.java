package bncp.pc.sensors;

import java.io.IOException;

/**
 * Basic Interface for a motor that can be stopped, started or turned to a specific rotation.
 * 
 * @author Micah
 *
 */
public interface EmulatedMotor {

	/**
	 * Rotate the motor for rotationDegrees degrees. If it is positive turn the motor forward or if rotationDegrees
	 * is negative turn the motor backwards. This method should block until the rotation is complete.
	 * @param rotationDegrees Number of degrees to turn the motor.
	 * @throws IOException If an IOException occurs.
	 */
	public void rotate(int rotationDegrees) throws IOException, InterruptedException;
	/**
	 * Start the motor in the specified direction.
	 * @param direction True to rotate forward, false to rotate backward
	 * @throws IOException
	 */
	public void start(boolean direction) throws IOException;
	/**
	 * Stop the motor. Optimally block until the motor is stopped.
	 * @throws IOException If one occurs
	 * @throws InterruptedException If one occurs
	 */
	public void stop() throws IOException, InterruptedException;
	/**
	 * Stop the motor and, if block is true, wait until the motor is stopped to return
	 * @param block If true block until the motor is stopped
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void stop(boolean block) throws IOException, InterruptedException;
	/**
	 * Gets the current rotation of the motor, in degrees.
	 * @return Rotation of the motor in degrees.
	 * @throws IOException If one occurs
	 * @throws InterruptedException If one occurs.
	 */
	public int getDegrees() throws IOException, InterruptedException;
	/**
	 * Clears the count of degrees that the motor has turned.
	 * @throws IOException
	 */
	public void clearCounter()  throws IOException;
	
}