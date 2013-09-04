package input;

public class Controller
{
	public double x, z, y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean running = false;
		
	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean run)
	{
		double rotationSpeed = 0.025;
		double walkSpeed = 0.6;
		double xMove = 0;
		double zMove = 0;
		double jumpheight = 0.5;
		double crouchheight = 0.35;
		
		if (forward)
		{
			zMove++;
			walk = true;
		}
		if (back)
		{
			zMove--;
			walk = true;
		}
		if (left)
		{
			xMove--;
			walk = true;
		}
		if (right)
		{
			xMove++;
			walk = true;
		}
		if (turnLeft)
		{
			rotationa -= rotationSpeed;
			walk = true;
		}
		if (turnRight)
		{
			rotationa += rotationSpeed;
			walk = true;
		}
		if (jump)
		{
			y += jumpheight;
		}
		if (crouch)
		{
			y -= crouchheight;
			run = false;
			walk = true;
			crouchWalk = true;
			walkSpeed = 0.3;
		}
		if (run)
		{
			walkSpeed = 25;
			walk = true;
			running = true;
		}
		
		if (!forward && !back && !left && !right && !turnLeft && !turnRight)
		{
			walk = false;
		}
		if (!crouch)
		{
			crouchWalk = false;
		}
		if (!run)
		{
			running = false;
		}
		
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		
		x += xa;
		y *= 0.9;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotation *= 0.5;
	}
}
