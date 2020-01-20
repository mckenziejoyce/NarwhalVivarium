/**
 * Vivarium.java - the vivarium and all the things inside it 
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 15 
 */
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium implements Displayable, Animate {
	private Tank tank;
	public ArrayList<Component> vivarium = new ArrayList<Component>();
	public ArrayList<Fish> fish = new ArrayList<Fish>();
	public ArrayList<Narwhal> narwhals = new ArrayList<Narwhal>();
	public ArrayList<Food> food = new ArrayList<Food>();
	public ArrayList<Food> foodInTank = new ArrayList<Food>();
	public boolean drawFood;
	public final Food foodOne;
	public final Food foodTwo;
	public final Food food3;
	public final Food food4;
	public final Food food5;
	public final Food food6;
	public final Food food7;
	public final Food food8;
	public final Food food9;
	public final Food food10;
	public final Food food11;
	public final Food food12;
	public final Food food13;
	public final Food food14;
	public final Food food15;
	public final Food food16;
	public final Food food17;
	public final Food food18;
	public int foodCount;
	public boolean schoolTogether;
	
	

	public Vivarium() {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		this.drawFood = false;
		//Create Narhwals and add to narwhal List
		Narwhal narwhalOne = new Narwhal(new Point3D(1.5,0,0), 0.15f,this);
		narwhals.add(narwhalOne);
		this.foodCount =0;
		this.schoolTogether = false;
		
		
		//Create Fish and add to fish List
		Fish fishOne = new Fish(new Point3D(randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5)), 0.4f,this);
		Fish fishTwo = new Fish(new Point3D(randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5)), 0.4f,this);
		Fish fishThree = new Fish(new Point3D(randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5)), 0.4f,this);
		Fish fishFour = new Fish(new Point3D(randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5), randomWithRange(-1.5, 1.5)), 0.4f,this);
		fish.add(fishOne);
		fish.add(fishTwo);
		fish.add(fishThree);
		//fish.add(fishFour);
		
		//Add Food and add to food list 
		this.foodOne = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.foodTwo = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food3 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food4 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food5 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food6 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food7 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food8 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food9 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food10 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food11 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food12 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food13 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food14 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food15 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food16 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food17 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		this.food18 = new Food(new Point3D(randomWithRange(-2, 2), 2, randomWithRange(-2, 2)), 0.2f,this);
		
		food.add(foodOne);
		food.add(foodTwo);
		food.add(food3);
		food.add(food4);
		food.add(food5);
		food.add(food6);
		food.add(food7);
		food.add(food8);
		food.add(food9);
		food.add(food10);
		food.add(food11);
		food.add(food12);
		food.add(food13);
		food.add(food14);
		food.add(food15);
		food.add(food16);
		food.add(food17);
		food.add(food18);
		
		//Add all creatures to vivarium 
		for(int i=0; i<narwhals.size();i++) {
			vivarium.add(narwhals.get(i));
		}
		for(int i=0; i<fish.size();i++) {
			vivarium.add(fish.get(i));
		}
		for(int i=0; i<food.size();i++) {
			vivarium.add(food.get(i));
		}
	}

	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for (Component object : vivarium) {
			object.initialize(gl);
		}
		for (Food object : food) {
			object.initialize(gl);
		}
	}
	//When F is pressed crate a piece of food at the top of the tank 
	public void createFood() {
		//Set the foods inTank to true since it was added and add it to that list
		food.get(foodCount).inTank = true;
		foodInTank.add(food.get(foodCount));
		foodCount +=1;
	}
	//Makes the fish school together - lets them all know they have to 
	public void schoolTogether() {
		if(this.schoolTogether == true) {
			for(int i=0;i<fish.size();i++) {
				fish.get(i).inSchool = true;
			}
			createNewLeaderFish();
		}
		else {
			for(int i=0;i<fish.size();i++) {
				fish.get(i).inSchool = false;
				fish.get(i).schoolLeader = false;
			}
		}
		
	}
	//Picks a leader fish for the others to follow 
	public void createNewLeaderFish() {
		for(int i=0;i<fish.size();i++) {
			if(fish.get(i).eaten == false) {
				fish.get(i).schoolLeader = true;
				break;
			}
		}
	}

	public void update(GL2 gl) {
		tank.update(gl);
		for (Narwhal object : narwhals) {
			object.update(gl);
		}
		for (Fish object : fish) {
			if(object.eaten == false)
				object.update(gl);
		}
		for (Food object : food) {
			if(object.eaten == false && object.inTank == true)
				object.update(gl);
		}
	}

	public void draw(GL2 gl) {
		tank.draw(gl);
		for (Narwhal object : narwhals) {
			object.draw(gl);
		}
		for (Fish object : fish) {
			if(object.eaten == false)
				object.draw(gl);
		}
		for (Food object : food) {
			if(object.eaten == false && object.inTank == true)
				object.draw(gl);
		}

	}
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}

	@Override
	public void animationUpdate(GL2 gl) {
		for (Component example : vivarium) {
			if (example instanceof Animate) {
				((Animate) example).animationUpdate(gl);
			}
		}
	}
	double randomWithRange(double min, double max)
	{
	   double range = (max - min);     
	   return (Math.random() * range) + min;
	}
}
