/**
 * NarwhalBodyDisplayable.java - a displayable for the narwhal body
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

public class NarwhalBodyDisplayable implements Displayable {
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	
	private int callListHandle;
	//The angle of the joint 
	private final double angle;
	private GLUquadric qd;
	private double radius;
	
	public NarwhalBodyDisplayable(final double radius, final double angle, final GLUT glut) {
		this.radius = radius;
		this.angle = angle;
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
		// position this so that the sphere is drawn above the x-y plane, not at the origin
		//gl.glRotated(-90, 0, 1, 0);
		gl.glTranslated(0, 0, this.radius);
		gl.glScalef(0.4f, 0.4f, 1);
		gl.glColor3f(0.29f, 0.44f, .55f);
		glut.glutSolidSphere(this.radius, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
	} 

}
