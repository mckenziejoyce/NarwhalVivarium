/**
 * FishTailDisplayable.java - a displayable for the fish tail 
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 12 
 */
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class FishTailDisplayable implements Displayable {
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	private int callListHandle;
	//The angle of the joint 
	private final double angle;
	private final double height;
	private GLUquadric qd;
	private double radius;
	
	public FishTailDisplayable(final double radius, final double height, final double angle, final GLUT glut) {
		//super(radius,glut);
		this.angle = angle;
		this.height = height;
		this.radius = radius;
	}
	
	@Override
	public void draw(final GL2 gl) {
		gl.glCallList(this.callListHandle);
	}
	@Override
	public void initialize(final GL2 gl) {
		this.callListHandle = gl.glGenLists(1);
		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		GLU glu = new GLU();
		this.qd = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glTranslated(-.23, 0, 0);
		gl.glRotated(-90, 0, 1, 0);
		gl.glScalef(-0.5f, 1, 1);
		gl.glScaled(-1,1, 1);
		gl.glTranslated(0, 0, -.2);
		gl.glTranslatef(0, 0, -0.5f);
		gl.glColor3f(0.8f, 0.5f, 0.2f);
		glut.glutSolidCone(this.radius, this.height, DEFAULT_SLICES, DEFAULT_STACKS);
		gl.glPopMatrix();
		gl.glEndList();
	}
}
