package bncp.pc.testing;

import javax.swing.JComponent;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GeometricPlane extends JComponent {

	private LinkedList<Double[]> cords = new LinkedList<Double[]>();
	private int cx,cy,ovalwidth,offset;

	public GeometricPlane(int xCenter, int yCenter, int pointSize){
		cx = xCenter;
		cy = yCenter;
		ovalwidth = pointSize;
		offset = ovalwidth/2;
		this.setPreferredSize(new Dimension(cx*2,cy*2));
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(Color.RED);
		g.fillOval(cx-offset, cy-offset,ovalwidth , ovalwidth);
		g.setColor(Color.GRAY);
		g.drawLine(0, cy, getWidth(), cy);
		g.drawLine(cx, 0, cx, getHeight());
		g.setColor(Color.BLACK);
		synchronized(cords){
			for(Double[] cord : cords){
				
				g.fillOval(((int)(cx + cord[0]))-offset, ((int)(cy - cord[1]))-offset, ovalwidth, ovalwidth);
				
			}
		}
	}
	
	public void addPoint(double x, double y){
		synchronized(cords){
			cords.add(new Double[]{x,y});
		}
		repaint();//apply changes
	}
	public void clearPoints(){
		synchronized(cords){
			cords.clear();
		}
		repaint();
	}

}
