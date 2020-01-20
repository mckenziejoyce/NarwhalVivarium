/**
 * Elipse.java - an ellipse object : used for narwhal body, narwhal fins, fish body, fish tail
 * 
 * McKenzie Joyce
 * 
 * CS480
 * 
 * Programming assignment 3 due Oct 15 
 */
import javax.media.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl



public class Ellipse extends Circular implements Displayable {
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	
	private int callListHandle;
	//The angle of the joint 
	private final double angle;
	//the type of the ellipse - either narwhal body, fin, etc 
	private final String type;
	
	public Ellipse(final double radius, final double angle, final String type, final GLUT glut) {
		super(radius,glut);
		this.angle = angle;
		this.type = type;
	}
	
	@Override
	public void draw(final GL2 gl) {
		gl.glCallList(this.callListHandle);
	}
	
	@Override
	public void initialize(final GL2 gl) {
		if(type.compareTo("narwhalBody") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			// position this so that the sphere is drawn above the x-y plane, not at the origin
			gl.glTranslated(0, 0, this.radius());
			gl.glScalef(0.4f, 0.4f, 1);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
//		if(type.compareTo("narwhalTailFinLeft") == 0) {
//			this.callListHandle = gl.glGenLists(1);
//			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
//			gl.glPushMatrix();
//			gl.glTranslated(0, 0, -.5);
//			gl.glScalef(0.2f, 0.2f, 0.3f);
//			gl.glRotated(90, 0, 0, 1);
//			this.glut().glutSolidSphere(this.radius(), 36, 18);
//			gl.glPopMatrix();
//			gl.glEndList();
//		}
//		if(type.compareTo("narwhalTailFinRight") == 0) {
//			this.callListHandle = gl.glGenLists(1);
//			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
//			gl.glPushMatrix();
//			// position this so that the sphere is drawn above the x-y plane, not at the origin
//			gl.glTranslated(0, 0, this.radius());
//			gl.glScalef(0.9f, 0.5f, 1);
//			this.glut().glutSolidSphere(this.radius(), 36, 18);
//			gl.glPopMatrix();
//			gl.glEndList();
//		}
		if(type.compareTo("narwhalFinLeft") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			gl.glTranslated(0, 0, -.3);
			gl.glScalef(0.15f, 0.15f, 0.3f);
			gl.glRotatef(70, 1, 0, 1);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
		if(type.compareTo("narwhalFinRight") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			gl.glTranslated(0, 0, -.3);
			gl.glScalef(0.15f, 0.15f, 0.3f);
			gl.glRotatef(70, 1, 0, 1);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
		if(type.compareTo("fishBody") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			// position this so that the sphere is drawn above the x-y plane, not at the origin
			gl.glScalef(0.4f, 0.6f, 1);
			gl.glTranslated(0, 0, -0.09f);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
		if(type.compareTo("tailJoint") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			// position this so that the sphere is drawn above the x-y plane, not at the origin
			gl.glScalef(0.1f, 0.1f, .1f);
			gl.glTranslated(0, 0, -0.09f);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		if(type.compareTo("fishTail") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			// position this so that the sphere is drawn above the x-y plane, not at the origin
			gl.glTranslated(0, 0, this.radius());
			gl.glScalef(0.9f, 0.5f, 1);
			this.glut().glutSolidSphere(this.radius(), 36, 18);
			gl.glPopMatrix();
			gl.glEndList();
		}
	}

}
