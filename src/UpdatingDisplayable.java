/**
 * An object which can draw itself, and update whatever state it maintains which
 * is necessary for drawing.
 * 
 * McKenzie Joyce 
 * CS480 PA3
 * 
 * Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 */


import javax.media.opengl.GL2;


public interface UpdatingDisplayable extends Displayable {
  /**
   * Updates the state of this object using the specified OpenGL object (for
   * example, redefining a GL call list based on a change to the state of this
   * object).
   * 
   * @param gl
   *          The OpenGL object on which to draw this object.
   */
  void update(final GL2 gl);
}
