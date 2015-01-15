package bncp.nxt.comm;

import java.io.IOException;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.addon.CompassHTSensor;
import bncp.nxt.ReplyPacket;
import bncp.nxt.SensorWrapper;
import bncp.nxt.io.GetPacket;
import bncp.nxt.io.InitPacket;
import bncp.nxt.io.MotorPacket;
import bncp.nxt.io.Packet;
import bncp.nxt.io.SetPacket;

public class BasicPacketHandler implements PacketHandler {

	SensorWrapper[] devices = new SensorWrapper[7];
	

	

	public Packet handlePacket(Packet pkt) throws IOException {

		if (pkt instanceof GetPacket)
			return handleGetPacket((GetPacket) pkt);
		else if (pkt instanceof MotorPacket)
			return handleMovePacket((MotorPacket) pkt);
		else if (pkt instanceof InitPacket)
			handleInitPacket((InitPacket) pkt);
		else if (pkt instanceof SetPacket)
			handleSetPacket((SetPacket) pkt);
		else
			System.out.println("Unknown packet type!");
		return null;//there are no replies to send.
	}

	private void handleInitPacket(InitPacket pkt) {
		if (pkt.device == Packet.MOTOR) {
			devices[pkt.port] = new SensorWrapper(getMotorPort(pkt.port));
		} else {// its a sensor.
			if (pkt.device == Packet.ULTRASONIC)
				devices[pkt.port] = new SensorWrapper(new UltrasonicSensor(
						getSensorPort(pkt.port)));
			else if (pkt.device == Packet.TOUCH)
				devices[pkt.port] = new SensorWrapper(new TouchSensor(
						getSensorPort(pkt.port)));
			else if (pkt.device == Packet.LIGHT)
				devices[pkt.port] = new SensorWrapper(new LightSensor(
						getSensorPort(pkt.port)));
			else if (pkt.device == Packet.HT_COLOR)
				devices[pkt.port] = new SensorWrapper(new ColorHTSensor(
						getSensorPort(pkt.port)));
			else if (pkt.device == Packet.HT_COMPASS)
				devices[pkt.port] = new SensorWrapper(new CompassHTSensor(
						getSensorPort(pkt.port)));
		}
		System.out.println("Init handled");
	}

	private Packet handleGetPacket(GetPacket pkt) throws IOException {
		int val = 0;
		System.out.println("get " + pkt.device + "@" + pkt.port);
		try {
			val = devices[pkt.port].read();
		} catch (NullPointerException e) {
			System.out.println("Requested Non-Initialized port!");
		}
		return new ReplyPacket(pkt.device, pkt.port, val);
	}

	private void handleSetPacket(SetPacket pkt) {
		if (pkt.type == SetPacket.MOTOR_ACCEL) {
			getMotorPort(pkt.port).setAcceleration(pkt.val);
		} else if (pkt.type == SetPacket.MOTOR_SPEED) {
			getMotorPort(pkt.port).setSpeed(pkt.val);
		} else {
			System.out.println("Invalid SetPacket!");
			Sound.buzz();
		}
	}

	private Packet handleMovePacket(MotorPacket pkt) throws IOException {
		NXTRegulatedMotor motor = getMotorPort(pkt.port);
		boolean noBlock = !pkt.confirm;// prevent blocks for efficiency or wait
										// til the operation is done.
		if (pkt.value == MotorPacket.INFINITE) {
			if (pkt.forward)
				motor.forward();
			else
				motor.backward();
		} else if (pkt.value == MotorPacket.STOP_CODE)
			motor.stop(noBlock);
		else if (pkt.value == MotorPacket.CLEAR)
			motor.resetTachoCount();
		else {

			if (!pkt.forward)
				pkt.value *= -1;
			motor.rotate(pkt.value, noBlock);
		}
		if (!noBlock) {
			System.out.println("Replied to mot");
			return new ReplyPacket(Packet.MOTOR, pkt.port,
					motor.getTachoCount());
		}
		
		System.out.println("Handled motor...");
		
		return null;
	}

	public SensorPort getSensorPort(int port) {
		if (port == Packet.PORT_1)
			return SensorPort.S1;
		else if (port == Packet.PORT_2)
			return SensorPort.S2;
		else if (port == Packet.PORT_3)
			return SensorPort.S3;
		else if (port == Packet.PORT_4)
			return SensorPort.S4;
		else {
			System.out.println("Invalid port!");
			return SensorPort.S1;
		}

	}

	public NXTRegulatedMotor getMotorPort(int port) {
		if (port == Packet.PORT_A)
			return Motor.A;
		if (port == Packet.PORT_B)
			return Motor.B;
		if (port == Packet.PORT_C)
			return Motor.C;

		System.out.println("Invalid motor port!");
		return Motor.A;

	}

}
