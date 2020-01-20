/**
 * Cone.java - used for narwhals horn 
 * 
 * McKenzie Joyce 
 * 
 * CS480 PA3 
 */
import javax.media.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Cone extends Circular implements Displayable {
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	private int callListHandle;
	//The angle of the joint 
	private final double angle;
	private final double height;
	private final String type;
	
	public Cone(final double radius, final double height, final double angle, final String type, final GLUT glut) {
		super(radius,glut);
		this.angle = angle;
		this.height = height;
		this.type = type;
	}
	
	@Override
	public void draw(final GL2 gl) {
		gl.glCallList(this.callListHandle);
	}
	public void initialize(final GL2 gl) {
		if(type.compareTo("narwhalHorn")==0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			//gl.glScaled(-1, 1, 1);
			gl.glTranslated(0, 0, this.radius());
			gl.glScaled(-1, 1, 1);
			//gl.glRotated(180, 0, 0, 1);
			this.glut().glutSolidCone(this.radius(), this.height, DEFAULT_SLICES, DEFAULT_STACKS);
			gl.glPopMatrix();
		}
		if(type.compareTo("fishTail") == 0) {
			this.callListHandle = gl.glGenLists(1);
			gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
			gl.glPushMatrix();
			//gl.glScalef(-0.5f, 1, 1);
			//gl.glScaled(-1,1, 1);
			//gl.glTranslatef(0, 0, -0.5f);
			this.glut().glutSolidCone(this.radius(), this.height, DEFAULT_SLICES, DEFAULT_STACKS);
			gl.glPopMatrix();
		}
	}

}
