/**
 * Coordinate class
 * Custom type used to model the coordinates of radar data used in the Decide problem.
 * The properties of the coordinate class are used when calculating the LICs inside the Decide class.
 */
public class Coordinate {
    double XPOS;
    double YPOS;

    /**
     * Custom type for single coordinate
     * @param xpos x-position
     * @param ypos y-position
     */
    Coordinate(double xpos, double ypos) {
        this.XPOS = xpos;
        this.YPOS = ypos;
    }
};