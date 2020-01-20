/**
 * An object which has angles around three axes and position
 * 
 * McKenzie Joyce 
 * CS480 PA3
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 */
public interface Configuration {

  /**
   * Gets the current angle at which this joint is rotated around the x axis.
   * 
   * @return The current angle at which this joint is rotated around the x
   *         axis.
   */
  double xAngle();

  /**
   * Gets the current angle at which this joint is rotated around the y axis.
   * 
   * @return The current angle at which this joint is rotated around the y
   *         axis.
   */
  double yAngle();

  /**
   * Gets the current angle at which this joint is rotated around the z axis.
   * 
   * @return The current angle at which this joint is rotated around the z
   *         axis.
   */
  double zAngle();
  
  /**
   * Gets the current position where local origin located at.
   * 
   * @return The current position
   */
  Point3D position();

  void setXAngle(final double xAngle);
  void setYAngle(final double yAngle);
  void setZAngle(final double zAngle);
  void setPosition(final Point3D p);
}
