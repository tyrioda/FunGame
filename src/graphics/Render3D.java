package graphics;

import input.Controller;

import java.util.Random;

import control.Game;

public class Render3D extends Render
{
	public double[] zBuffer;
	private double renderDistance = 5000;

	public Render3D(int width, int height)
	{
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void floor(Game game)
	{
		double floorPosition = 8;
		double ceilingPosition = 20;
		double forward = game.controls.z;
		double right = game.controls.x;
		double up = game.controls.y;
		double walking = Math.sin(game.time / 6.0) * 0.4;

		double rotation = game.controls.rotation;
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);

		if (Controller.running)
		{
			walking = Math.sin(game.time / 6.0) * 0.8;
		}

		if (Controller.crouchWalk)
		{
			walking = Math.sin(game.time / 6.0) * 0.2;
		}

		for (int y = 0; y < height; y++)
		{
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;

			if (Controller.walk)
			{
				z = (floorPosition + up + walking) / ceiling;
			}

			if (ceiling < 0)
			{
				z = (ceilingPosition - up) / -ceiling;
				if (Controller.walk)
				{
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}

			for (int x = 0; x < width; x++)
			{
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];

				if (z > 300) // dont draw pixels beyond this limit
				{
					pixels[x + y * width] = 0;
				}
			}
		}

		/**
		 * Drawing walls
		 */
		Random random = new Random(100);
		for (int i = 0; i < 10000; i++)
		{
			double xx = 100;
			double yy = 100;
			double zz = 2;


			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);
			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height)
			{
				pixels[xPixel + yPixel * width] = 0xffff;
			}
		}

	}

	public void renderDistanceLimit()
	{
		int size = width * height;
		for (int i = 0; i < size; i++)
		{
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0)
			{
				brightness = 0;
			}

			if (brightness > 255)
			{
				brightness = 255;
			}

			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
