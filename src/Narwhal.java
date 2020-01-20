/**
 * Narwhal.java - a narwhal object
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
/**
 * Body : Ellipse 
 * 
 * Horn : Cone 
 * 
 * Tail : 2 scaled spheres
 * 
 */
public class Narwhal extends Component implements Animate{
	//Rotate Speed 
	private double rotateSpeed = 1;
	
	//Speed and direction for narwhal 
	private float speed_x;
	private float dir_x;
	private float speed_y; 
	private float dir_y;
	private float speed_z; 
	private float dir_z;
	// The OpenGL utility toolkit object. 
	private final GLUT glut = new GLUT();
	// The body to be modeled 
	private final Component body;
	//The horn/ tusk to be modeled
	private final Component horn;
	//The left fin on the body to be modeled
	private final Component finleft;
	//The right fin on the body to be modeled
	private final Component finright;
	//The color of the narwhal 
	public static final FloatColor NARWHAL_COLOR = FloatColor.GRAYBLUE;
	//The color of the tusk 
	public static final FloatColor TUSK_COLOR = FloatColor.GRAY;
	//The radius of the components which comprise the Narwhal. */
	public static final double NARWHAL_RADIUS = 2;
	//The radius of the body 
	public static final double BODY_RADIUS = 2;
	//The radius of the base of the Horn 
	public static final double HORN_BASE_RADIUS = 0.5;
	//The height of the horn 
	public static final double HORN_HEIGHT = 6;
	// The x, y, and z values of the narwhal 
	public double x;
	public double y;
	public double z;
	//The vivarium that the narhwal is in 
	public Vivarium vivarium;
	//The fish the Narwhal is going after 
	public Fish targetFish;


	
	//Creating a narwhal 
	public Narwhal(final Point3D p, float scale, Vivarium v) {
		super(new Point3D(p));
		this.setScale(scale);
		this.x = p.x();
		this.y = p.y();
		this.z = p.z();
		this.vivarium = v;
		//Set to null because we can not assume there is a fish in the tank 
		this.targetFish = null;
		
		//Speed and Direction for Narwhal 
		speed_x = 0.008f; speed_y = 0.008f; speed_z = 0.008f;
		dir_x = -1; dir_y = -1; dir_z = -1;
		
		//Create the parts of the narwhal 
		this.body = new Component(new Point3D(0, 0, 0), new NarwhalBodyDisplayable(BODY_RADIUS, 85, this.glut), "narwhalBody");
		this.horn = new Component(new Point3D(0, 0, 0),new NarwhalHornDisplayable(HORN_BASE_RADIUS, HORN_HEIGHT, 180, this.glut), "narwhalHorn");
		this.finleft = new Component(new Point3D(0, 0, 0), new NarwhalLeftFinDisplayable(BODY_RADIUS, 85, this.glut), "narwhalLeftFin");
		this.finright = new Component(new Point3D(0, 0, 0), new NarwhalRightFinDisplayable(BODY_RADIUS, 85, this.glut), "narwhalRightFin");
		
		//Set up how the narwhal is connected and put it and all its parts in the right position 
		this.addChild(body);
		body.addChild(horn);
		body.addChild(finleft);
		finleft.addChild(finright);
		this.rotate(Axis.Y, -90);
		finleft.rotate(Axis.X, 75);
		finright.rotate(Axis.X, 150);
		//Extents for the tail so it moves back and forth - only left fin since right is connected to left 
		finleft.setYNegativeExtent(-25);
		finleft.setYPositiveExtent(25);
	}
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
	
	//Update the narwhal 
	@Override
	public void animationUpdate(GL2 gl) {
		//If it hit its extent go the other way - makes it have natural tail movement 
		if (finleft.checkRotationReachedExtent(Axis.Y)) {
			rotateSpeed = -rotateSpeed;
		}
		//Rotate the tail 
		finleft.rotate(Axis.Y, rotateSpeed);
		finleft.rotate(Axis.Z, rotateSpeed);
		//Get the point before it is moved and rotated
		Point3D fromVector = new Point3D(this.x,this.y,this.z);
		//Get the coordinates for where it will go 
		move();
		// Get the point that is at once it moves
		Point3D toVector = new Point3D(this.x,this.y,this.z);
		//Find the rotation axis and angle 
		float[] rotMatrix = faceDirection(fromVector, toVector);
		double rotAngle = faceDirectionAngle(fromVector, toVector);
		//Rotate it using the rotation axis and angle and put it where it should be 
		this.rotate(rotMatrix, rotAngle);
		this.setPosition(new Point3D(this.x,this.y,this.z));
	}

	//Set the x, y, and z values to where it should move to 
	public void move() {
		double min = -4;
		double max = 4;
		Point3D curP = new Point3D(this.x,this.y,this.z);
		Point3D destinationCord;
		//Vector for chasing prey 
		double chasePreyVectorX = 0;
		double chasePreyVectorY= 0;
		double chasePreyVectorZ= 0;
	
		//Vector for swimming around aimlessly
		double randomVectorX = 0;
		double randomVectorY = 0;
		double randomVectorZ = 0;
		
		//Checks to see if the target fish is eaten and if it is sets to null since it already got that one
		if(this.targetFish != null && this.targetFish.eaten == true) {
			this.targetFish = null;
		}
		//If it has no target fish it checks for a new target fish - the closest one to it in the tank 
		if(this.targetFish == null) {
			double DistOfFish = 100000;
			for(int i=0; i<vivarium.fish.size();i++) {
				Fish possTargetFish = vivarium.fish.get(i);
				if(possTargetFish.eaten == false) {
					double possxDistOfFish = possTargetFish.x - this.x;
					double possyDistOfFish = possTargetFish.y - this.y;
					double posszDistOfFish = possTargetFish.z - this.z;
					double possDistOfFish = Math.sqrt(Math.pow(possxDistOfFish, 2)+Math.pow(possyDistOfFish, 2)+Math.pow(posszDistOfFish, 2));
					if(possDistOfFish < DistOfFish) {
						targetFish = possTargetFish;
						DistOfFish = possDistOfFish;
					}
				}
			}
			
		}
		// If there is a fish for it to go after make it go in the direction of that fish to chase and eat it 
		if(this.targetFish != null) {
			destinationCord = targetFish.position();
			// Use potential function to get the chase prey vectors 
			Point3D potChasingPrey = potentialF(curP, destinationCord, 0.03);
			chasePreyVectorX = potChasingPrey.x();
			chasePreyVectorY = potChasingPrey.y();
			chasePreyVectorZ = potChasingPrey.z();
		}
		//If there is no target fish at this point it means there are no fish in the tank so it swims around with no purpose 
		if(this.targetFish == null) {
			double randomX = min + Math.random( ) * (max - min);
			double randomY = min + Math.random( ) * (max - min);
			double randomZ = min + Math.random( ) * (max - min);
			destinationCord = new Point3D(randomX, randomY, randomZ);
			// Use potential function to get the random vectors 
			Point3D potRandmom = potentialF(curP, destinationCord, 0.04);
			randomVectorX = potRandmom.x();
			randomVectorY = potRandmom.y();
			randomVectorZ = potRandmom.z();
		}
		
		//Adjusts the direction accoridingly - if prey random is 0, if no prey chase prey is 0 so only essentially using one of the two even tho it sums 
		dir_x += chasePreyVectorX + randomVectorX;
		dir_y += chasePreyVectorY + randomVectorY;
		dir_z += chasePreyVectorZ + randomVectorZ;

	    if(curP.x() + this.scale() > 1.75 || curP.x() - this.scale() < -1.75) {
	    	// Need to turn around and go opposite X direction 
	    	dir_x = -dir_x;
	    	//Tells the component class the narwhal changed its X direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionXNar = true;
	    }
	    if(curP.y() + this.scale() > 2 || curP.y() - this.scale() < -2) {
	    	// Need to turn around and go opposite Y direction 
	    	dir_y = -dir_y;
	    	//Tells the component class the narwhal changed its Y direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionYNar = true;
	    }
	    if(curP.z() + this.scale() > 2 || curP.z() - this.scale() < -2) {
	    	// Need to turn around and go opposite Z direction 
	    	dir_z = -dir_z;
	    	//Tells the component class the narwhal changed its Z direction - used in Component.rotate(float[] axis, final double angleDelta) 
	    	this.changeDirectionZNar = true;
	    }
	    
	    //Sets the points of the narwhal to the new points by seeing where it would be knowing the direction its going in and its speed 
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
	
}

