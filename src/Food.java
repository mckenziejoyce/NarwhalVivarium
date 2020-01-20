/**
 * Food.java - a food object
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 15 
 */
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;

class Food extends Component implements Animate {
	//The rotation speed of food
	private double rotateSpeed = 1;
	//The X, Y, and Z values of the foods location 
	public double x;
	public double y;
	public double z;
	//The vivarium the fish in it - used to access other objects in the tank 
	public Vivarium vivarium;
	//The food radius 
	public static final double FOOD_RADIUS = .5;
	//A boolean value to tell if the food has been eaten 
	public boolean eaten;
	//The piece of food
	private final Component foodpiece;
	//The speed the food moves down the tank 
	private float speed;
	//A boolean value to tell if the food is in the tank 
	public boolean inTank;
	//A boolean value to tell if the food is at the bottom of the tank - if it is fish wont go after it since 
	//it is out of bounds
	public boolean atBottom;
	
	public Food(Point3D p, float scale, Vivarium v) {
		super(new Point3D(p));
		this.setScale(scale);
		this.x = p.x();
		this.y = p.y();
		this.z = p.z();
		this.vivarium = v;
		this.eaten = false;
		this.speed = 0.005f;
		this.inTank = false;
		this.atBottom = false;
		//Create the piece of food 
		this.foodpiece = new Component(new Point3D(0,0,0), new FoodDisplayable(FOOD_RADIUS));
		//Allow it to be in the vivarium 
		this.addChild(foodpiece);
	}
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
	
	
	@Override
	public void animationUpdate(GL2 gl) {
		//Get the position of the food 
		Point3D curP = this.position();
		//Check if any of the fish in the vivarium were able to eat this food and if one has set eaten to true
		for(int i=0; i<vivarium.fish.size();i++) {
			Fish scaryFish = vivarium.fish.get(i);
			Point3D fishCord = scaryFish.position();
			boolean dead = collision(curP,fishCord);
			if(dead == true) {
				this.eaten = true;
				this.inTank = false;
				vivarium.foodInTank.remove(this);
			}
		}
		//If its in the tank and hasnt been eaten make it fall to the bottom and once its at the bottom sit there 
		if(this.inTank == true && this.eaten==false) {
			if(this.y > -1.9) {
				this.y = this.y-speed;
				this.setPosition(new Point3D(this.x, this.y, this.z));
			}
			else {
				this.setPosition(new Point3D(this.x, this.y, this.z));
				this.atBottom = true;
			}
		}
	}
	//Check for collision 
	public boolean collision(Point3D a, Point3D b) {
		
		double xDiff = a.x() - b.x();
		double yDiff = a.y() - b.y();
		double zDiff = a.z() - b.z();
		
		if (Math.abs(xDiff) < 0.3 & Math.abs(yDiff) < 0.3 & Math.abs(zDiff) < 0.3) {
			return true;
		}

		return false; 
	}
	
}
