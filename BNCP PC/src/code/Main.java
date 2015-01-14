package code;
import java.io.IOException;
import roboIO.pc.EmulatedSensor;
import lejos.pc.comm.NXTCommException;
import roboIO.Packet;
import roboIO.pc.NXTPConnection;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NXTCommException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, NXTCommException, InterruptedException {
		NXTPConnection conn = new NXTPConnection();
		conn.init();
		conn.setDaemon(true);
		conn.start();
		EmulatedSensor comp = conn.getEmulatedSensor(Packet.ULTRASONIC, Packet.PORT_4);
		conn.rotateMotor(Packet.PORT_A, 145, true);
		conn.clearMotorTachometer(Packet.PORT_A);
		EmulatedSensor mot = conn.getEmulatedSensor(Packet.MOTOR, Packet.PORT_A);
		while(true){
			//System.out.println(conn.getSensorValue(Packet.HT_COMPASS, Packet.PORT_3));
			System.out.println(comp.read());
			Thread.sleep(100);
		}
		
		
		/*EmulatedMotor mot = conn.getEmulatedMotor(Packet.PORT_B);
		
		for(int i = 0; i < 10000; i++){
			mot.start(true);
			System.out.println("Forward");
			Thread.sleep(100);
			conn.startMotor(Packet.PORT_B, false);
			System.out.println("Backward");
			Thread.sleep(100);
			mot.stop();
			System.out.println("Stopped");
			Thread.sleep(100);
		}*///import roboIO.Packet;
		//import roboIO.pc.EmulatedMotor;
		//import roboIO.pc.NXTPConnection;
		/*
		JFrame window = new JFrame("RC");
		window.add(new RCPanel(conn.getEmulatedMotor(Packet.PORT_B),conn.getEmulatedMotor(Packet.PORT_C)));
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		window.pack();
		window.setVisible(true);*/
		/*
		while(sysin.hasNext()){
			conn.rotateMotor(sysin.nextInt(), sysin.nextInt());
			System.out.print("next: ");
		}*/
		/*conn.initSensor(Packet.HT_COMPASS, Packet.PORT_3);
		int val = 0, preval = val, diff = 0;
		for(int c = 1; c < 100; c++){
			val = conn.getSensorValue(Packet.HT_COMPASS, Packet.PORT_3);
			System.out.println("Moved " + (val - preval));
			preval = val;
			Thread.sleep(100);
		}
		System.out.println("Done...");
		conn.close();*/
		/*// TODO Auto-generated method stub
		int test = 0xff;
		int a = (test&0xf9)>>3;
		int b = (test&0x7);
		System.out.println("from " + test + " decoded " + a + " and " + b);
		while(true){
			System.out.println("enter a");
			a = sysin.nextInt();
			System.out.println("enter b");
			b = sysin.nextInt();
			int c = (a&0x1F)<<3 | (b&0x7);
			System.out.println("Decoded: " + c);
			
		}*/
		/*Scanner sysin = new Scanner(System.in);
		if(sysin.nextLine().equalsIgnoreCase("server")){
			ServerSocket ssock = new ServerSocket(1232);
			Socket sock = ssock.accept();
			System.out.println("Server Connected!");

			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			PacketIO io = new PacketIO(in,out);
			OldPacket pkt = new OldPacket(OldPacket.SET_PACKET,OldPacket.ULTRASONIC,OldPacket.PORT_1,255,255);
			io.send(pkt);
			ptln("Sent: " + pkt);
		}
		else{//do client
			Socket sock = new Socket("localhost",1232);
			ptln("Client connected");
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			PacketIO io = new PacketIO(in,out);
			ptln(io.read()+"");
			
		}*/
	}
	public static void ptln(String str){
		System.out.println(str);
	}

}
