package bncp.pc.testing.pinch.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import bncp.pc.sensors.EmulatedMotor;


public class MovementControlPanel extends JPanel implements MouseListener,
		KeyListener, ActionListener {

	private EmulatedMotor left, right;

	/**
	 * Initialize the MovementControlPanel with leftMotor as the left motor and
	 * rightMotor as the right motor. Movement will be done accordingly. ie
	 * turning left will make right motor go forward, and the left motor go
	 * backwards.
	 * 
	 * @param leftMotor
	 *            Left motor of the robot.
	 * @param rightMotor
	 *            Right motor of the robot.
	 */
	public MovementControlPanel(EmulatedMotor leftMotor,
			EmulatedMotor rightMotor, int yolo) {
		left = leftMotor;
		right = rightMotor;
	}

	private boolean[] state = new boolean[] { false, false, false, false },
			oldstate = new boolean[] { false, false, false, false };
	private final static int FORWARD = 0, BACKWARD = 1, LEFT = 2, RIGHT = 3;
	private EmulatedMotor L, R;
	private Timer keyTrigger;// On linux systems, when a key is held down it
								// generates multiple keyPressed() and
								// keyRelease() events. In order to prevent the
								// motors stopping and starting on each
								// keyRelease/keyPressed, a timer is started
								// when a key is pressed, each time
								// keyReleased() is called the timer is
								// restarted. When the timer actually triggers,
								// you know that the user has released the key

	public MovementControlPanel(EmulatedMotor leftMotor,
			EmulatedMotor rightMotor) {
		L = leftMotor;
		R = rightMotor;
		keyTrigger = new Timer(60, this);
		keyTrigger.setRepeats(false);

		JButton front = new JButton("/\\"), back = new JButton("\\/"), left = new JButton(
				"<"), right = new JButton(">");
		front.setName("forward");
		back.setName("backward");
		left.setName("left");
		right.setName("right");

		JPanel spacer = new JPanel(), spacer2 = new JPanel();
		// spacer.setPreferredSize(new Dimension(5, 5));
		// spacer2.setPreferredSize(new Dimension(5, 5));

		front.addMouseListener(this);// add all the key listeners!
		back.addMouseListener(this);
		left.addMouseListener(this);
		right.addMouseListener(this);
		front.addKeyListener(this);
		back.addKeyListener(this);
		left.addKeyListener(this);
		right.addKeyListener(this);
		spacer.addKeyListener(this);
		spacer2.addKeyListener(this);

		setLayout(new GridLayout(2, 3));
		add(spacer);
		add(front);
		add(spacer2);
		add(left);
		add(back);
		add(right);

	}

	public void updateMotors() {
		try {

			for (int i = 0; i < state.length; i++) {
				if (state[i] != oldstate[i]) {
					if (state[i] == true) {// start the motor
						switch (i) {
						case FORWARD:
							L.start(true);
							R.start(true);
							break;
						case BACKWARD:
							L.start(false);
							R.start(false);
							break;
						case LEFT:
							L.start(false);
							R.start(true);
							break;
						case RIGHT:
							L.start(true);
							R.start(false);
							break;
						}
					} else {// stop the motor.
						L.stop(false);
						R.stop(false);
					}
				}

			}
			oldstate = Arrays.copyOf(state, state.length);
		} catch (IOException | InterruptedException e) {
			System.err.println("IOException in RCPanel!");
			e.printStackTrace();
		}
	}

	@Override
	public void mousePressed(MouseEvent ae) {
		String name = ((JButton) ae.getSource()).getName();
		switch (name) {
		case "forward":
			state[FORWARD] = true;
			break;
		case "backward":
			state[BACKWARD] = true;
			break;
		case "left":
			state[LEFT] = true;
			break;
		case "right":
			state[RIGHT] = true;
			break;
		default:
			System.out.println("Invalid ActionEvent!");
		}
		updateMotors();// reflect the changes.
	}

	@Override
	public void mouseReleased(MouseEvent ae) {
		String name = ((JButton) ae.getSource()).getName();
		switch (name) {
		case "forward":
			state[FORWARD] = false;
			break;
		case "backward":
			state[BACKWARD] = false;
			break;
		case "left":
			state[LEFT] = false;
			break;
		case "right":
			state[RIGHT] = false;
			break;
		default:
			System.out.println("Invalid ActionEvent!");
		}
		updateMotors();// reflect the changes.
	}


	/**
	 * Interpret key events to see if a change of state is necessary.
	 * @param ke
	 */
	@Override
	public void keyPressed(KeyEvent ke) {

		if (!keyTrigger.isRunning()) {
			switch (ke.getKeyCode()) {

			case KeyEvent.VK_UP:
				state[FORWARD] = true;
				break;
			case KeyEvent.VK_DOWN:
				state[BACKWARD] = true;
				break;
			case KeyEvent.VK_LEFT:
				state[LEFT] = true;
				break;
			case KeyEvent.VK_RIGHT:
				state[RIGHT] = true;
				break;

			}
			updateMotors();
			keyTrigger.start();
		} else
			keyTrigger.restart();

	}

	/**
	 * When a key is released, update the motor state.
	 * @param ke
	 */
	@Override
	public void keyReleased(KeyEvent ke) {

		switch (ke.getKeyCode()) {

		case KeyEvent.VK_UP:
			state[FORWARD] = false;
			break;
		case KeyEvent.VK_DOWN:
			state[BACKWARD] = false;
			break;
		case KeyEvent.VK_LEFT:
			state[LEFT] = false;
			break;
		case KeyEvent.VK_RIGHT:
			state[RIGHT] = false;
			break;

		}
		keyTrigger.restart();
	}

	/**
	 * When the timer triggers, update the motors.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateMotors();
	}
	
	//--------------------------------------------------------------------
	// Below this line are unused methods required by the MouseListener and
	// KeyListener interfaces.
	//--------------------------------------------------------------------
	
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
