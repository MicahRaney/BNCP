package bncp.nxt;
import java.io.IOException;

import bncp.nxt.io.GetPacket;
import bncp.nxt.io.InitPacket;
import bncp.nxt.io.MotorPacket;
import bncp.nxt.io.Packet;
import bncp.nxt.io.PacketIO;
import bncp.nxt.io.SetPacket;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;


public class USBConnectionServer extends Thread {

	SensorWrapper[] devices = new SensorWrapper[7];
	PacketIO io = null;
	
	/**
	 * This will initialize the USB connection, and will block until an exception occurs or a connection
	 * is found.
	 */
	public void init(){

		USBConnection conn;
		System.out.println("Waiting for connection");
		do{
			conn = USB.waitForConnection(1000, NXTConnection.RAW);
	
		}while(conn == null);
		System.out.println("Connection Found!");
		io = new PacketIO(conn);//initialize connection.
		
		System.out.println("IO Initialized");
	}
	
	@Override
	public void run(){
		if (io == null)//if the thread isn't initialized, initialize it!
			init();
		System.out.println("Server starting...");
		try{
			int c = 0;
			while(!isInterrupted()){
				LCD.drawInt(c, 0, 0);
				Packet pkt = io.read();//read a packet.
				System.out.println("Got pkt");
				handlePacket(pkt);
				c++;
			}
			System.out.println("Shutting down...");
		}
		catch(IOException e){
			System.out.println("IOException!");
			e.printStackTrace();
			Button.waitForAnyPress();
		}
	}
	
	private void handlePacket(Packet pkt) throws IOException{
		if(pkt instanceof GetPacket)
			handleGetPacket((GetPacket)pkt);
		else if (pkt instanceof MotorPacket)
			handleMovePacket((MotorPacket)pkt);
		else if(pkt instanceof InitPacket)
			handleInitPacket((InitPacket)pkt);
		else if(pkt instanceof SetPacket)
			handleSetPacket((SetPacket)pkt);
		else
			System.out.println("Unknown packet type!");
	}
	
	private void handleInitPacket(InitPacket pkt){
		if(pkt.device == Packet.MOTOR){
			devices[pkt.port] = new SensorWrapper(getMotorPort(pkt.port));
		}
		else{//its a sensor.
			if(pkt.device == Packet.ULTRASONIC)
				devices[pkt.port] = new SensorWrapper(new UltrasonicSensor(getSensorPort(pkt.port)));
			else if(pkt.device == Packet.TOUCH)
				devices[pkt.port] = new SensorWrapper(new TouchSensor(getSensorPort(pkt.port)));
			else if(pkt.device == Packet.LIGHT)
				devices[pkt.port] = new SensorWrapper(new LightSensor(getSensorPort(pkt.port)));
			else if(pkt.device == Packet.HT_COLOR)
				devices[pkt.port] = new SensorWrapper(new ColorHTSensor(getSensorPort(pkt.port)));
			else if(pkt.device == Packet.HT_COMPASS)
				devices[pkt.port] = new SensorWrapper(new CompassHTSensor(getSensorPort(pkt.port)));
		}
		System.out.println("Init handled");
	}
	private void handleGetPacket(GetPacket pkt) throws IOException{
		int val = 0;
		try{
			val = devices[pkt.port].read();
		}
		catch(NullPointerException e){
			System.out.println("Requested Non-Initialized port!");
		}
		io.send(new ReplyPacket(pkt.device, pkt.port, val));
	}
	
	private void handleSetPacket(SetPacket pkt){
		if(pkt.type == SetPacket.MOTOR_ACCEL){
			getMotorPort(pkt.port).setAcceleration(pkt.val);
		}
		else if(pkt.type == SetPacket.MOTOR_SPEED){
			getMotorPort(pkt.port).setSpeed(pkt.val);
		}
		else{
			System.out.println("Invalid SetPacket!");
			Sound.buzz();
		}
	}
	
	private void handleMovePacket(MotorPacket pkt) throws IOException{
		NXTRegulatedMotor motor = getMotorPort(pkt.port);
		boolean noBlock = !pkt.confirm;//prevent blocks for efficiency or wait til the operation is done.
		if(pkt.value == MotorPacket.INFINITE){
			if(pkt.forward)
				motor.forward();
			else
				motor.backward();
		}
		else if(pkt.value == MotorPacket.STOP_CODE)
			motor.stop(noBlock);
		else if(pkt.value == MotorPacket.CLEAR)
			motor.resetTachoCount();
		else{

			if(!pkt.forward)
				pkt.value *= -1;
			motor.rotate(pkt.value, noBlock);
		}
		if(!noBlock){
			io.send(new ReplyPacket(pkt.device,pkt.port,motor.getTachoCount()));
			System.out.println("Replied to mot");
		}
		System.out.println("Handled motor...");
	}

	public SensorPort getSensorPort(int port){
		if(port == Packet.PORT_1)
			return SensorPort.S1;
		else if(port == Packet.PORT_2)
			return SensorPort.S2;
		else if(port == Packet.PORT_3)
			return SensorPort.S3;
		else if(port == Packet.PORT_4)
			return SensorPort.S4;
		else{
			System.out.println("Invalid port!");
			return SensorPort.S1;
		}
		
	}
	public NXTRegulatedMotor getMotorPort(int port){
		if(port == Packet.PORT_A)
			return Motor.A;
		if(port == Packet.PORT_B)
			return Motor.B;
		if(port == Packet.PORT_C)
			return Motor.C;
		
		System.out.println("Invalid motor port!");
		return Motor.A;
		
		
	}
}
