/**
 * Fish.java - a fish object
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 12 
 */
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;

public class Fish extends Component implements Animate {
	private double rotateSpeed = .6;
	//Speed and direction for fish 
	private float speed_x;
	private float dir_x; 
	private float speed_y; 
	private float dir_y;
	private float speed_z; 
	private float dir_z;
	// The OpenGL utility toolkit object. 
	private final GLUT glut = new GLUT();
	//The pointed tip of the body where head is to be modeled
	//private final Component head;
	////The majority of the body to be modeled
	private final Component fishbody;
	//The joint/little stick straight out part of the tail to be modeled
	private final Component tail;
	//The color of the fish 
	public static final FloatColor FISH_COLOR = FloatColor.ORANGE;
	//The height of the head triangle
	public static final double BODY_RADIUS = 0.2;
	//The size of the base of the tail 
	public static final double TAIL_BASE = 0.1;
	//The height of the tail 
	public static final double TAIL_HEIGHT = 0.35;
	//The X,Y, and Z values of the fish coord 
	public double x;
	public double y;
	public double z;
	//The vivarium the fish in it - used to access other objects in the tank 
	public Vivarium vivarium;
	//Whether or not this fish has been eaten 
	public boolean eaten;
	//The piece of food the fish is trying to eat 
	public Food targetFood;
	//The narwhal the fish is trying to swim away from 
	public Narwhal scaryNarwhal;
	//A boolean to determine whether or not the fish should create a school
	public boolean inSchool;
	//A boolean to determine if this fish is the schools leader 
	public boolean schoolLeader; 
	
	//Create a fish object 
	public Fish(final Point3D p, float scale, Vivarium v) {
		super(new Point3D(p));
		this.setScale(scale);
		this.x = p.x();
		this.y = p.y();
		this.z = p.z();
		this.vivarium = v;
		this.eaten = false;
		this.targetFood = null;
		this.scaryNarwhal = null;
		this.inSchool = false;
		this.schoolLeader = false;
		
		//Speed and Direction for Fish 
		speed_x = 0.007f; speed_y = 0.007f; speed_z = 0.007f;
		dir_x = -1; dir_y = -1; dir_z = -1;

		//Create the parts of the fish 
		this.fishbody = new Component(new Point3D(0, 0, 0), new FishBodyDisplayable(BODY_RADIUS, 85, this.glut), "fishBody");
		this.tail = new Component(new Point3D(0, 0, 0),new FishTailDisplayable(TAIL_BASE, TAIL_HEIGHT, 180, this.glut), "fishTail");
		
		//Set up how the fish is connected 
		this.addChild(fishbody);
		fishbody.addChild(tail);
		
		//Set extents for the fish tail to give it natural movement 
		tail.setYNegativeExtent(-7);
		tail.setYPositiveExtent(7);
		
	}
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
	
	
	@Override
	public void animationUpdate(GL2 gl) {
		//If this fish has been eaten remove it from the list of fish in the vivarium 
		if(this.eaten == true) {
			vivarium.fish.remove(this);
		}
		
		// Rotate the tail in a natural back and forth way 
		if (tail.checkRotationReachedExtent(Axis.Y)) {
			rotateSpeed = -rotateSpeed;
		}
		tail.rotate(Axis.Y, rotateSpeed);
		//For fish that either arent in a school or are the leaders of the school 
		if(this.inSchool == false || (this.inSchool == true && this.schoolLeader == true)) {
			//Get the point before it moves then find the new X,Y, and Z values for where to move it
			Point3D fromVector = new Point3D(this.x,this.y,this.z);
			move();
			//Get the point it will be at after it moves
			Point3D toVector = new Point3D(this.x,this.y,this.z);
			//Find the axis of rotation and angle of rotation needed for facing the direction its moving 
			float[] rotMatrix = faceDirection(fromVector, toVector);
			double rotAngle = faceDirectionAngle(fromVector, toVector);
			//Set the new position and rotate it to face the way its moving 
			this.setPosition(new Point3D(this.x,this.y,this.z));
			this.rotate(rotMatrix, rotAngle);
		}
		//For the fish that are just followng the leader 
		if(this.inSchool == true && this.schoolLeader == false) {
			//Get the point before it moves then find the new X,Y, and Z values for where to move it
			Point3D fromVector = new Point3D(this.x,this.y,this.z);
			Fish leaderFish = null;
			for(int i=0;i<vivarium.fish.size();i++) {
				if(vivarium.fish.get(i).schoolLeader == true && vivarium.fish.get(i).eaten == false) {
					leaderFish = vivarium.fish.get(i);
				}
			}
			if(leaderFish == null) {
				vivarium.createNewLeaderFish();
				for(int i=0;i<vivarium.fish.size();i++) {
					if(vivarium.fish.get(i).schoolLeader == true) {
						leaderFish = vivarium.fish.get(i);
					}
				}
			}
			moveInSchool(leaderFish);
			//Get the point it will be at after it moves
			Point3D toVector = new Point3D(this.x,this.y,this.z);
			//Find the axis of rotation and angle of rotation needed for facing the direction its moving 
			float[] rotMatrix = faceDirection(fromVector, toVector);
			double rotAngle = faceDirectionAngle(fromVector, toVector);
			//Set the new position and rotate it to face the way its moving 
			this.setPosition(new Point3D(this.x,this.y,this.z));
			this.rotate(rotMatrix, rotAngle);
		}
		
	}
	
	// Set the X,Y,and Z values to where it will move to 
	public void move() {	
		double min = -4;
		double max = 4;
		Point3D curP = new Point3D(this.x,this.y,this.z);
		
		//Vector for random movement - set to 0 since wont be used unless no food or narwhals
		double randomVectorX = 0;
		double randomVectorY = 0;
		double randomVectorZ = 0;
		
		//Vector for getting food - set to 0 first in case no food
		double foodVectorX = 0;
		double foodVectorY = 0;
		double foodVectorZ = 0;
		
		//Vector for predator - set to 0 in case no predator is in tank 
		double predatorVectorX = 0;
		double predatorVectorY = 0;
		double predatorVectorZ = 0;
		
		//If there are narwhals in the tank use the one closest to run away from 
		if(scaryNarwhal == null) {
			if(vivarium.narwhals.size() != 0) {
				scaryNarwhal = vivarium.narwhals.get(0);
				double xDistOfScaryNarwhal = scaryNarwhal.x - this.x;
				double yDistOfScaryNarwhal = scaryNarwhal.y - this.y;
				double zDistOfScaryNarwhal = scaryNarwhal.z - this.z;
				double DistOfScaryNarwhal = Math.sqrt(Math.pow(xDistOfScaryNarwhal, 2)+Math.pow(yDistOfScaryNarwhal, 2)+Math.pow(zDistOfScaryNarwhal, 2));
				for(int i =0; i< vivarium.narwhals.size(); i++) {
					Narwhal possScaryNarwhal = vivarium.narwhals.get(i);
					double possxDistOfScaryNarwhal = scaryNarwhal.x - this.x;
					double possyDistOfScaryNarwhal = scaryNarwhal.y - this.y;
					double posszDistOfScaryNarwhal = scaryNarwhal.z - this.z;
					double possDistOfScaryNarwhal = Math.sqrt(Math.pow(possxDistOfScaryNarwhal, 2)+Math.pow(possyDistOfScaryNarwhal, 2)+Math.pow(posszDistOfScaryNarwhal, 2));
					if(possDistOfScaryNarwhal > DistOfScaryNarwhal) {
						scaryNarwhal = possScaryNarwhal;
						DistOfScaryNarwhal = possDistOfScaryNarwhal;
					}
				}
				
			}
		}
		//If there is a narwhal in the tank swim away from it 
		if(scaryNarwhal != null) {
			Point3D narwhalCord = scaryNarwhal.position();
			Point3D potFunPred = potentialF(curP, narwhalCord,0.01);
			predatorVectorX = potFunPred.x();
			predatorVectorY = potFunPred.y();
			predatorVectorZ = potFunPred.z();
			//Check to see if narwhal ate the fish 
			boolean dead = narlyCollision(narwhalCord,curP);
			if(dead == true) {
				this.eaten = true;
			}
		}
		
		//If the target food has been eaten
		if(targetFood != null && targetFood.eaten == true) {
			targetFood = null;
		}
		//If there is no target food check if there is any accessible and if so make it the target food
		if(targetFood == null) {
			double DistOfFood = 100000;
			for(int i=0;i<vivarium.food.size();i++) {
				Food possyummyFood = vivarium.food.get(i);
				if(possyummyFood.inTank == true && possyummyFood.atBottom == false) {
					double possxDistOfFood = possyummyFood.x - this.x;
					double possyDistOfFood = possyummyFood.y - this.y;
					double posszDistOfFood = possyummyFood.z - this.z;
					double possDistOfFood = Math.sqrt(Math.pow(possxDistOfFood, 2)+Math.pow(possyDistOfFood, 2)+Math.pow(posszDistOfFood, 2));
					if(possDistOfFood < DistOfFood) {
						targetFood = possyummyFood;
						DistOfFood = possDistOfFood;
					}
				}
				
			}
		}
		//If there is a target food to go after calculate the values using potential function 
		if(targetFood != null) {
			Point3D foodLocation = targetFood.position();
			Point3D potFunFood = potentialF(curP, foodLocation,.04);
			foodVectorX = potFunFood.x();
			foodVectorY = potFunFood.y();
			foodVectorZ = potFunFood.z();
			boolean collided = collision(curP,foodLocation);
			if(collided == true) {
				foodVectorX = 0;
				foodVectorY = 0;
				foodVectorZ = 0;
			}
		}
		// If theres no narwhal to run away from and no food to go after swim around with no purpose 
		if(targetFood == null && scaryNarwhal == null) {
			double randomX = min + Math.random( ) * (max - min);
			double randomY = min + Math.random( ) * (max - min);
			double randomZ = min + Math.random( ) * (max - min);
			Point3D destinationCord = new Point3D(randomX, randomY, randomZ);
			Point3D potRandom = potentialF(curP, destinationCord, 0.02);
			randomVectorX = potRandom.x();
			randomVectorY = potRandom.y();
			randomVectorZ = potRandom.z();
		}
		
		//Calculate the sums of all the different potential functions for food, narwhals, and random (random is only not zero when no narwhals or food)
		double sumX = randomVectorX + foodVectorX + (-predatorVectorX);
		double sumY = randomVectorY + foodVectorY + (-predatorVectorY);
		double sumZ = randomVectorZ + foodVectorZ + (-predatorVectorZ); 	
		dir_x += sumX;
		dir_y += sumY;
		dir_z += sumZ;
		


	    if(curP.x() + this.scale() > 2 || curP.x() - this.scale() < -2) {
	    	// Need to turn around and go opposite X direction 
	    	dir_x = -dir_x;
	    	//Tells the component class the fish changed its X direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionXFish = true;
	    }
	    if(curP.y() + this.scale() > 2 || curP.y() - this.scale() < -2) {
	    	// Need to turn around and go opposite Y direction 
	    	dir_y = -dir_y;
	    	//Tells the component class the fish changed its Y direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionYFish = true;
	    }
	    if(curP.z() + this.scale() > 2 || curP.z() - this.scale() < -2) {
	    	// Need to turn around and go opposite Z direction
	    	dir_z = -dir_z;
	    	//Tells the component class the fish changed its Z direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionZFish = true;
	    }
	    //Sets the points of the fish to the new points by seeing where it would be knowing the direction its going in and its speed 
	    this.x += speed_x*dir_x;
	    this.y += speed_y*dir_y;
	    this.z += speed_z*dir_z;
	}
	
	//Calculate Potential Function - Gaussian  
	public Point3D potentialF(Point3D p, Point3D q, double priority) {
			
		//Expression needed to calculate partial derivatives
		double expr = Math.pow(p.x()-q.x(),2) + Math.pow(p.y()-q.y(),2) + Math.pow(p.z()-q.z(),2);
			
		//Scaler to determine priority 
		double scalar = priority;
			
		//Calculate Partial Derivatives
		double partialX = -2*(p.x() - q.x())*Math.exp(-expr);
		double partialY = -2*(p.y() - q.y())*Math.exp(-expr);
		double partialZ = -2*(p.z() - q.z())*Math.exp(-expr); 
			
		//Use the partial derivatives and the priority to create the correct points and return them 
		Point3D potential = new Point3D(scalar*partialX,scalar*partialY,scalar*partialZ);
		return potential; 
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
	//Check for collision with Narwhal 
	public boolean narlyCollision(Point3D narwhal, Point3D fish) {
		//+.3 so it eats from its mouth not butt
		double xDiff = narwhal.x() - fish.x();
		double yDiff = narwhal.y()+.3 - fish.y();
		double zDiff = narwhal.z() - fish.z();
		
		if (Math.abs(xDiff) < 0.3 & Math.abs(yDiff) < 0.3 & Math.abs(zDiff) < 0.3) {
			return true;
		}

		return false; 
	}
	//Function to get the rotation axis needed to make it face the right direction 
	public float[] faceDirection(Point3D fromVector, Point3D toVector) {
		Quaternion orientation = new Quaternion();
		Point3D rotMatrix = fromVector.crossProduct(toVector);
		fromVector.norm();
		toVector.norm();
		double rotAngle = Math.cos(fromVector.dotProduct(toVector));
		Quaternion rotQuat = new Quaternion((float)rotAngle, (float)rotMatrix.x(), (float)rotMatrix.y(), (float)rotMatrix.z());
		orientation = rotQuat.multiply(orientation);
		float[] orientationMatrix = orientation.to_matrix();
		return orientationMatrix;

	}
	//Function to get the rotation angle needed to make it face the right direction 
	public double faceDirectionAngle(Point3D fromVector, Point3D toVector) {
		fromVector.norm();
		toVector.norm();
		double rotAngle = Math.cos(fromVector.dotProduct(toVector));
		return rotAngle;
	}
	//Move function if the fish are in a school and not the leader 
	public void moveInSchool(Fish leaderFish) {
		double min = -4;
		double max = 4;
		Point3D curP = new Point3D(this.x,this.y,this.z);
		Point3D des = leaderFish.position();
		Point3D newPoint = potentialF(curP, des,.05);
		dir_x += newPoint.x();
		dir_y += newPoint.y();
		dir_z += newPoint.z();

	    if(curP.x() + this.scale() > 2 || curP.x() - this.scale() < -2) {
	    	// Need to turn around and go opposite X direction 
	    	dir_x = -dir_x;
	    	//Tells the component class the fish changed its X direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionXFish = true;
	    }
	    if(curP.y() + this.scale() > 2 || curP.y() - this.scale() < -2) {
	    	// Need to turn around and go opposite Y direction 
	    	dir_y = -dir_y;
	    	//Tells the component class the fish changed its Y direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionYFish = true;
	    }
	    if(curP.z() + this.scale() > 2 || curP.z() - this.scale() < -2) {
	    	// Need to turn around and go opposite Z direction
	    	dir_z = -dir_z;
	    	//Tells the component class the fish changed its Z direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionZFish = true;
	    }
	    //Sets the points of the fish to the new points by seeing where it would be knowing the direction its going in and its speed 
	    this.x += speed_x*dir_x;
	    this.y += speed_y*dir_y;
	    this.z += speed_z*dir_z;
	}
}

