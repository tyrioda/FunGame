package input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener
{
	public boolean[] key = new boolean[68836];
	public static int MouseX;
	public static int MouseY;
	
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = true;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = false;
		}
		
	}
	
	public void focusLost(FocusEvent e)
	{
		for (int i = 0; i < key.length; i++)
		{
			key[i] = false;
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		MouseX = e.getX();
		MouseY = e.getY();
	}

	public void mouseDragged(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void keyTyped(KeyEvent e){}
	public void focusGained(FocusEvent e){}	
}
