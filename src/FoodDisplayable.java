/**
 * FoodDisplayable.java - a food object drawn 
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 15 
 */
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

public class FoodDisplayable implements Displayable{
	// The OpenGL handle to the display list which contains all the components which
	//comprise this cylinder.
	private int callListHandle;
	public double radius;
	private GLUT glut;
	private GLUT glu;

	/**
	 * Instantiates this object with the specified radius and OpenGL utility toolkit
	 * object for drawing the sphere.
	 *
	 * @param radius The radius of this object.
	 * @param glut   The OpenGL utility toolkit object for drawing the sphere.
	 */
	public FoodDisplayable(final double radius) {
		this.radius = radius;
	}

	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.callListHandle);
	}
		
	//Defines the OpenGL call list which draws a scaled sphere representing the food
	@Override
	public void initialize(final GL2 gl) {
		this.callListHandle = gl.glGenLists(1);
		// create a grain of food by scaling a sphere
		GLU glu = new GLU();
		GLUT glut = new GLUT();
		
		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		gl.glPushMatrix();
		// position this so that the sphere is drawn above the x-y plane, not at
		// the origin
		gl.glTranslated(0, 0, radius);
		gl.glScalef(0.5f, 0.4f, 1);
		gl.glColor3f(0.36f, 0.2f, .09f);
		glut.glutSolidSphere(radius, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
	}

}
