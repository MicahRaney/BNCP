package bncp.pc.sensors;

import java.io.IOException;

import bncp.pc.io.GetPacket;
import bncp.pc.io.MotorPacket;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.Packet;
import bncp.pc.io.SetPacket;

public class NXTPMotor implements EmulatedMotor {

	private final BNCPConnection conn;
	private final int port;

	public NXTPMotor(BNCPConnection connection, int port) {
		conn = connection;
		this.port = port;
	}

	@Override
	public void rotate(int rotation, boolean block) throws IOException,
			InterruptedException {
		boolean forward = true;
		if (rotation < 0) {
			forward = false;
			rotation *= -1;
		}
		Packet pkt = new MotorPacket(port, rotation, forward, block);
		if (block)
			conn.waitForReply(pkt, Packet.MOTOR, port);
		else
			conn.send(pkt);
	}

	@Override
	public void start(boolean direction) throws IOException {
		conn.send(new MotorPacket(port, 0, direction, false));
	}

	@Override
	public void stop() throws IOException, InterruptedException {
		stop(true);
	}

	@Override
	public int getDegrees() throws IOException, InterruptedException {
		return conn.waitForReply(new GetPacket(Packet.MOTOR, port),
				Packet.MOTOR, port).value;
	}

	@Override
	public void clearTachometer() throws IOException {
		conn.send(new MotorPacket(port, MotorPacket.CLEAR, false, true));
	}

	@Override
	public void stop(boolean block) throws IOException, InterruptedException {
		Packet pkt = new MotorPacket(port, -1, false, block);
		if (block)
			conn.waitForReply(pkt, Packet.MOTOR, port);
		else
			conn.send(pkt);
	}

	/**
	 * (from lejos.nxt.NXTRegulatedMotor javadoc) Sets desired motor speed , in
	 * degrees per second; The maximum reliably sustainable velocity is 100 x
	 * battery voltage under moderate load, such as a direct drive robot on the
	 * level.
	 * 
	 * Note: Setting this to zero will stop the motor. Additionally, if the
	 * motor is stopped, setting a speed will not start the motor.
	 * 
	 * @param speed
	 *            Speed of motor in degrees per second.
	 * @throws IOException
	 *             If an IOException Occurs
	 */
	public void setSpeed(int speed) throws IOException {
		SetPacket pkt = new SetPacket(Packet.MOTOR, port,
				SetPacket.MOTOR_SPEED, speed);
		// send a packet for the motor at at the predetermined port. Set motor
		// speed to val_speed.
		conn.send(pkt);
	}

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
	public void setAcceleration(int acceleration) throws IOException {
		SetPacket pkt = new SetPacket(Packet.MOTOR, port,
				SetPacket.MOTOR_ACCEL, acceleration);// compose a packet to set
														// acceleration
		conn.send(pkt);
	}

}
