package bncp.pc.testing.pinch.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bncp.pc.sensors.EmulatedMotor;
import bncp.pc.testing.pinch.Lang;

public class MotorControlPanel extends JPanel implements ChangeListener {

	private LinkedList<EmulatedMotor> motors = new LinkedList<EmulatedMotor>();
	private JSlider accelSlider, speedSlider;

	public MotorControlPanel() {

		// Initialize the two sliders
		accelSlider = new JSlider(JSlider.VERTICAL);
		accelSlider.setName("acceleration-slider");
		accelSlider.setMaximum(10000);
		accelSlider.setMinimum(100);
		accelSlider.setPaintTicks(true);
		accelSlider.setMajorTickSpacing(1000);
		accelSlider.setMinorTickSpacing(500);
		accelSlider.setValue(6000);
		accelSlider.addChangeListener(this);

		speedSlider = new JSlider(JSlider.VERTICAL);
		speedSlider.setName("speed-slider");
		speedSlider.setMaximum(10000);
		speedSlider.setMinimum(100);
		speedSlider.setPaintTicks(true);
		speedSlider.setMajorTickSpacing(1000);
		speedSlider.setMinorTickSpacing(500);
		speedSlider.setValue(360);
		speedSlider.addChangeListener(this);

		// initialize the two sub-panels. One for the acceleration slider
		// and one for the speed slider
		JPanel subAccel = new JPanel(), subSpeed = new JPanel();

		subAccel.setLayout(new BorderLayout());
		subAccel.add(new JLabel(Lang.ACCEL_SLIDER_LABEL), BorderLayout.NORTH);
		subAccel.add(accelSlider, BorderLayout.CENTER);

		subSpeed.setLayout(new BorderLayout());
		subSpeed.add(new JLabel(Lang.SPEED_SLIDER_LABEL), BorderLayout.NORTH);
		subSpeed.add(speedSlider, BorderLayout.CENTER);

		// initialize this Panel
		setLayout(new GridLayout(1, 2));
		add(subSpeed);
		add(subAccel);

	}

	public void addMotor(EmulatedMotor mot) {
		synchronized(motors){
			motors.add(mot);	
		}
	}

	public boolean removeMotor(EmulatedMotor mot) {
		boolean ret;
		synchronized(motors){
			ret = motors.remove(mot);	
		}
		return ret;
	}

	/**
	 * Called when a slider is changed to a new value. Updates all the motor's
	 * speed or acceleration (depending on which slider was interacted with).
	 * 
	 * @param evt
	 *            ChangeEvent for the slider that has been interacted with.
	 */
	public void stateChanged(ChangeEvent evt) {


		if(evt.getSource() == speedSlider){//change the speed.
			
			int speed = speedSlider.getValue();//get the new speed
			
			synchronized(motors){
				for(EmulatedMotor mot : motors)
					try {
						mot.setSpeed(speed);
					} catch (IOException e) {
						System.out.println("Uh-Oh! Setting the speed on a motor failed! (in MotorControlPanel)");
						e.printStackTrace();
					}
			}

			
		}
		else if(evt.getSource() == accelSlider){//change the acceleration
			
			int accel = accelSlider.getValue();//get the new acceleration
			
			synchronized(motors){
				for(EmulatedMotor mot : motors)
					try {
						mot.setAcceleration(accel);
					} catch (IOException e) {
						System.out.println("Uh-Oh! Setting the acceleration on a motor failed! (in MotorControlPanel)");
						e.printStackTrace();
					}
			}

		}
		else{//It wasn't the speed slider or the acceleration slider!
			System.out.println("MotorControlPanel: Got a ChangeEvent that wasn't from one of our sliders...");
		}
		

	}

}
