package code;

import java.awt.Dimension;
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

import roboIO.pc.EmulatedMotor;

public class RCPanel extends JPanel implements MouseListener, KeyListener,
		ActionListener {

	private boolean[] state = new boolean[] { false, false, false, false },
			oldstate = Arrays.copyOf(state, state.length);//
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

	public RCPanel(EmulatedMotor leftMotor, EmulatedMotor rightMotor) {
		L = leftMotor;
		R = rightMotor;
		keyTrigger = new Timer(40, this);
		keyTrigger.setRepeats(false);
		JButton front = new JButton("/\\"), back = new JButton("\\/"), left = new JButton(
				"<"), right = new JButton(">");
		front.setName("forward");
		back.setName("backward");
		left.setName("left");
		right.setName("right");

		JPanel spacer = new JPanel(), spacer2 = new JPanel();
		spacer.setPreferredSize(new Dimension(50, 50));
		spacer2.setPreferredSize(new Dimension(50, 50));

		front.addMouseListener(this);
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
			System.out.println("new state: " + Arrays.toString(state));
		} catch (IOException | InterruptedException e) {
			System.out.println("IOException in RCPanel! Resuming...");
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

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

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Boo!");
		updateMotors();
	}
}
