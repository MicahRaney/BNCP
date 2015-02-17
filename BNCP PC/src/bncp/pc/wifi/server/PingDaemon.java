package bncp.pc.wifi.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import bncp.pc.io.MotorPacket;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.Packet;
import bncp.pc.wifi.Common;

public class PingDaemon implements PingListener, ActionListener {

	/**
	 * If no ping is
	 */
	private volatile int alarmTime;
	private volatile long lastPing = 0;
	private Timer timer;
	private BNCPConnection conn;

	public PingDaemon(BNCPConnection conn, int alarmTime) {
		this.conn = conn;
		this.alarmTime = alarmTime;

		timer = new Timer(alarmTime, this);

	}

	public void start(){
		lastPing = System.currentTimeMillis();
		timer.start();
	}
	public void stop(){
		timer.stop();
	}
	
	@Override
	public void pingRecieved(long time) {
		lastPing = time;
		timer.restart();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			Thread.sleep(alarmTime);
			if (System.currentTimeMillis() - lastPing > alarmTime) {
				System.out.println("No pings recieved in "
						+ (System.currentTimeMillis() - lastPing)
						+ " milliseconds! Stopping motors as safety measure!");
				conn.send(new MotorPacket(Packet.PORT_A, MotorPacket.STOP_CODE,
						false, false));
				conn.send(new MotorPacket(Packet.PORT_B, MotorPacket.STOP_CODE,
						false, false));
				conn.send(new MotorPacket(Packet.PORT_C, MotorPacket.STOP_CODE,
						false, false));
			}
		} catch (InterruptedException | IOException e) {
			System.out.println("Exception in PingDaemon!");
			e.printStackTrace();
		}
	}

}
