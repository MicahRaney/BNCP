package roboIO.pc;
import java.io.IOException;
import java.util.LinkedList;

import roboIO.GetPacket;
import roboIO.InitPacket;
import roboIO.MovePacket;
import roboIO.Packet;
import roboIO.PacketIO;
import roboIO.ReplyPacket;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class NXTPConnection extends Thread {
	
	private volatile LinkedList<PendingHolder> pending = new LinkedList<PendingHolder>();
	private NXTComm conn; 
	private PacketIO io;
	/**
	 * Attempts to connect to a Mindstorms NXT Connected over USB.
	 * @param args
	 * @throws NXTCommException If one occurs or if no NXT can be found.
	 */
	public void init() throws NXTCommException {
		
	
		conn = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo[] nxts = conn.search(null);
		if(nxts.length == 0){
			System.out.println("No NXT Found!");
			throw new NXTCommException("No NXT Found!");
		}
		System.out.println("Connecting to the first NXT available...");
		conn.open(nxts[0],NXTComm.RAW);
		
		System.out.println("Connected! Initializing IO...");
		io = new PacketIO(conn);
		System.out.println("IO Initialized!");
			
			
	}
	
	@Override
	public void run(){
		
		try{

			while(!this.isInterrupted()){
				Packet pkt = read();
				if(pkt instanceof ReplyPacket)//is there a thread waiting for this packet?
					synchronized(pending){
						int id = pkt.getDevicePortCode();
						for(PendingHolder hld:pending){
							if(hld.ID == id){
								synchronized(hld){
									hld.reply = (ReplyPacket)pkt;
									hld.notifyAll();
								}
							}
						}
					}
			}
			
		}
		catch(Exception e){
			System.out.println("Something happened in NXTPConnection!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a packet over the connection.
	 * @param pkt Packet to send.
	 * @throws IOException If an IOException occurs.
	 */
	private synchronized void send(Packet pkt) throws IOException{
		io.send(pkt);
	}
	
	/**
	 * Reads a packet from the connection.
	 * @return Packet
	 * @throws IOException if one occurs.
	 */
	private Packet read() throws IOException{
		return io.read();
	}
	
	public void initSensor(int sensor, int port) throws IOException{
		send(new InitPacket(sensor,port));
	}
	
	public int getSensorValue(int sensor, int port) throws IOException, InterruptedException{
		
		return waitForReply(new GetPacket(sensor,port),sensor,port).value;

		//throw new IllegalArgumentException("This method can't be called yet :|... Not implemented!");
		/*Integer packetId = OldPacket.getEncodedDevicePort(sensor, port);
		pending.add(packetId);
		//send(new OldPacket(OldPacket.GET_PACKET, sensor, port, 0 , 0));
		packetId.wait();*/
	}
	
	public void clearMotorTachometer(int port)throws IOException{
		send(new MovePacket(Packet.MOTOR,port,MovePacket.CLEAR, false, true));
	}
	
	public void rotateMotor(int port, int rotation, boolean block) throws IOException, InterruptedException{
		boolean forward = true;
		if(rotation < 0){
			forward = false;
			rotation *= -1;
		}
		Packet pkt = new MovePacket(Packet.MOTOR,port, rotation, forward, block);
		if(block)
			waitForReply(pkt,Packet.MOTOR,port);
		else
			send(pkt);
		
	}
	
	public void startMotor(int port, boolean isForward) throws IOException{
		send(new MovePacket(Packet.MOTOR, port, 0, isForward, false));
	}
	public void stopMotor(int port, boolean block) throws IOException, InterruptedException{
		Packet pkt = new MovePacket(Packet.MOTOR, port, -1, false, block);
		if(block)
			waitForReply(pkt,Packet.MOTOR,port);
		else
			send(pkt);
	}

	public EmulatedSensor getEmulatedSensor(int device, int port) throws IOException{
		initSensor(device,port);
		return new NXTPSensor(this,device,port);
	}
	public EmulatedMotor getEmulatedMotor(int port){
		return new NXTPMotor(this, port);
	}
	
	private ReplyPacket waitForReply(Packet toSend, int sensor, int port) throws IOException, InterruptedException{
		
		PendingHolder hld = new PendingHolder(Packet.getEncodedDevicePort(sensor, port));
		synchronized(pending){
			pending.add(hld);
			send(toSend);
		}

		synchronized(hld){
			hld.wait();
		}
		return hld.reply;
		
	}
	
	/*
	public void run(){
		try{
			this.sleep(10);
		}
		catch(InterruptedException e){
			
		}
		while(!this.isInterrupted()){
			
		}
	}
	
	/**
	 * Stops the connection thread.
	 */
	/*public void kill(){
		this.interrupt();
	}*/
	
	/**
	 * Attempts to close the connection with the NXT
	 * @throws IOException If an IOException occurs
	 */
	public void close() throws IOException{
		conn.close();
	}
	
	public static class PendingHolder{
		public int ID;
		public ReplyPacket reply;
		public PendingHolder(int id){
			this.ID = id;
		}
		@Override
		public boolean equals(Object obj){
			if(((PendingHolder)obj).ID == ID)
				return true;
			else
				return false;
		}
	}
}
