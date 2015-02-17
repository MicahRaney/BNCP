package bncp.pc.testing.pinch;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import lejos.pc.comm.NXTCommException;
import bncp.pc.io.BNCPConnection;
import bncp.pc.io.BNCPConnectionFactory;
import bncp.pc.io.Packet;
import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.testing.pinch.gui.MotorControlPanel;
import bncp.pc.testing.pinch.gui.MovementControlPanel;

public class MovementControlTest {

	public static void main(String[] args) throws NXTCommException, IOException {
		
		BNCPConnection conn = BNCPConnectionFactory.getUSBConnection();
		
		EmulatedMotor a = conn.getEmulatedMotor(Packet.PORT_A), b = conn.getEmulatedMotor(Packet.PORT_B);

		a.setAcceleration(2000);
		b.setAcceleration(2000);

		JFrame window = new JFrame("Movement Control Debugger");
		window.add(new MovementControlPanel(a,b));
		window.setSize(new Dimension(150,100));
		window.setVisible(true);
		
		
		JFrame window2 = new JFrame("Spec Controls");
		window2.add(new MotorControlPanel());
		window2.pack();
		window2.setVisible(true);
	
	}

}
