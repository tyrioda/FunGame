package graphics;

import java.util.Random;

import control.Game;


public class Screen extends Render
{
	private Render test;
	private Render3D render;

	public Screen(int width, int height)
	{
		super(width, height);
		test = new Render(256, 256);
		Random random = new Random();
		render = new Render3D(width, height);

		int size = 256 * 256;
		for (int i = 0; i < size; i++)
		{
			test.pixels[i] = random.nextInt() * (random.nextInt(5)/4);
		}
	}

	public void render(Game game)
	{
		for (int i = 0; i < width * height; i++)
		{
			pixels[i] = 0;
		}

		render.floor(game);
		render.renderDistanceLimit();
		draw(render, 0, 0);
	}
}