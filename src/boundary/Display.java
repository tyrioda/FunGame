package boundary;

import graphics.Screen;
import input.Controller;
import input.InputHandler;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import control.Game;

public class Display extends Canvas implements Runnable
{
	/**
	 * Display class with JFrame
	 */
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "FunGame 0.0.3";
	
	private static boolean showFps = true;
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	private Thread thread;
	private BufferedImage img;
	private boolean running = false;
	private Screen screen;
	private int[] pixels;
	private Game game;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	private int fps;
	
	public Display()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	private void start()
	{
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run()
	{
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running)
		{
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();

			while (unprocessedSeconds > secondsPerTick)
			{
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0)
				{
					if (showFps == true)
					{
						fps = frames;
					}
					previousTime += 1000;
					frames = 0;
				}
			}

			if (ticked)
			{
				render();
				frames++;
			}
			render();
			frames++;
			
			newX = InputHandler.MouseX;
			if (newX > oldX)
			{
				System.out.println("right");
				Controller.turnRight = true;
			}
				
			if (newX < oldX)
			{
				System.out.println("left");
				Controller.turnLeft = true;
			}
				
			if (newX == oldX)
			{
				Controller.turnLeft = false;
				Controller.turnRight = false;
			}
			
			oldX = newX;
		}
	}

	private void tick()
	{
		game.tick(input.key);
	}

	private void render()
	{
		BufferStrategy s = this.getBufferStrategy();
		if (s == null)
		{
			createBufferStrategy(3); // 3D
			return;
		}

		screen.render(game);

		int size = WIDTH * HEIGHT;
		for (int i = 0; i < size; i++)
		{
			pixels[i] = screen.pixels[i];
		}

		Graphics g = s.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH + 10, HEIGHT + 10, null);
		g.setFont(new Font("Verdana", 0, 40));
		g.setColor(Color.YELLOW);
		g.drawString(fps + " FPS", 20, 50);
		g.dispose();
		s.show();
	}

	public void stop()
	{
		if (!running)
			return;
		running = false;
		try
		{
			thread.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

	}

	public static void main(String[] args)
	{
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		frame.getContentPane().setCursor(blank);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle(TITLE);
		frame.setVisible(true);

		game.start();
		System.out.println("Running...");
	}
}
