/**
 * Circular.java - a circular object
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignmnet 3 due Oct 15 
 */

import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

public class Circular {
	/** The OpenGL utility toolkit object to use to draw this object. */
	private final GLUT glut;
	/** The radius of this object. */
	private final double radius;
	
	//Constructor - creates a food object 
	public Circular(final double radius, final GLUT glut) {
		this.radius = radius;
		this.glut = glut;
	}
	
	//Gets the OpenGL utility toolkit object
	protected GLUT glut() {
		return this.glut;
	}
	
	//Gets the radius of the food object 
	protected double radius() {
		return this.radius;
	}
	
	
}
