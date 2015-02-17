package bncp.pc.io;

/**
 * 
 * @author Micah Raney
 *
 */
public class PacketIODebugger implements PacketIO{

	public void send(Packet pkt){
		System.out.println("Send: " + pkt.toString());
	}
	public Packet read(){
		
		System.out.println("Requested packet from IO... Waiting for .notify on PacketIODebugger");
		synchronized(this){
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Uh-Oh! PacketIODebugger.read() had an InterruptedException while sleeping!");
				e.printStackTrace();
			}	
		}
		
		return new ReplyPacket(0,0,0);
		
	}

}
