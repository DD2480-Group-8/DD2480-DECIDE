import java.util.Arrays;

public class Decide {

    enum CONNECTORS {
        NOTUSED,
        ORR,
        ANDD
    };

    int NUMPOINTS;
    Coordinate[] POINTS;
    CONNECTORS[][] LCM;
    boolean[][] PUM;
    boolean[] CMV = new boolean[15];
    boolean[] PUV;
    boolean[] FUV;
    Parameters PARAMETERS;


    Decide(int numpts, Coordinate[] pts, Parameters params, CONNECTORS[][] LCM, boolean[] PUV){
        this.NUMPOINTS = numpts;
        this.POINTS = pts;
        this.PARAMETERS = params;
        this.LCM = LCM;
        this.PUV = PUV;
    }

    public void LIC0() {
        for (int i = 1; i < NUMPOINTS; i++) {
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i - 1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i - 1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }


    /*
    * Implementation of LIC2: There exists at least one set of three consecutive data points which form an angle such that: angle < (PI − EPSILON)
    * or angle > (PI + EPSILON). The second of the three consecutive points is always the vertex of the angle. If either the first point or the 
    * last point (or both) coincides with the vertex, the angle is undefined and the LIC is not satisfied by those three points.
    */
    public void LIC2(){
        if(NUMPOINTS < 3){
            CMV[2] = false;
            return;
        }
        for(int i = 0; i < NUMPOINTS-2; i++){
            double angle = checkAngle(POINTS[i], POINTS[i+1], POINTS[i+2]);
            if( angle > Math.PI + PARAMETERS.EPSILON || angle < Math.PI - PARAMETERS.EPSILON){
                CMV[2] = true;
                return;
            }
        }
        CMV[2] = false;
    }

    /*
    * Helper function that figures out the angle formed by 3 Coordinates. 
    * The second argument is the vertex of the angle. 
    */
    public double checkAngle(Coordinate i, Coordinate j, Coordinate k){
        i = coordSubtract(i,j);
        k = coordSubtract(k,j);
        if((i.XPOS == 0 && i.YPOS == 0) || (k.XPOS == 0 && k.YPOS == 0)){
            return Math.PI;
        }
        double dotProduct = i.XPOS * k.XPOS + i.YPOS * k.YPOS;
        double lenProduct = Math.sqrt(Math.pow(i.XPOS,2) + Math.pow(i.YPOS,2)) * Math.sqrt(Math.pow(k.XPOS,2) + Math.pow(k.YPOS,2));
        return Math.acos(dotProduct/lenProduct);
    }

    /*
    * Helper function that subtracts one Coordinate from another. 
    */
    public Coordinate coordSubtract(Coordinate i, Coordinate j){
        Coordinate res = new Coordinate(0,0);
        res.XPOS = i.XPOS - j.XPOS;
        res.YPOS = i.YPOS - j.YPOS;
        return(res);
    }



    /**
     * Implementation of LIC1: There exists at least one set of three consecutive data points that cannot all be contained
     * within or on a circle of radius RADIUS1.
     * Finds the center between three points, and checks if there is any point that is a distance greater than RADIUS1
     * from the center.
     */
    public void LIC1() {
        for (int i = 2; i < NUMPOINTS; i++) {
            // Find the centroid.
            Coordinate centroid = new Coordinate(
                    (POINTS[i].XPOS + POINTS[i-1].XPOS + POINTS[i-2].XPOS) / 3,
                    (POINTS[i].YPOS + POINTS[i-1].YPOS + POINTS[i-2].YPOS) / 3
            );
            // Check if any of the points have a distance to the centroid larger than the radius.
            if (
                    (Math.sqrt(Math.pow(POINTS[i].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                            || (Math.sqrt(Math.pow(POINTS[i-1].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i-1].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                            || (Math.sqrt(Math.pow(POINTS[i-2].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i-2].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
            ) {
                CMV[1] = true;
                break; // only need one set of points to fulfill this, no need to continue the loop.
            }
        }
    }

    /**
    *   LIC 3 is:
    *   There exists at least one set of three consecutive data points that are the vertices of a triangle
    *   with area greater than AREA1. (0 ≤ AREA1)
    *
    */
    public void LIC3(){
        if (NUMPOINTS < 3) {
            CMV[3] = false;
            return;
        }

        for (int n = 0 ; n < NUMPOINTS-2 ; n++) {
            // Extract the coordinates of the three consecutive data points
            double x1 = POINTS[n].XPOS;
            double y1 = POINTS[n].YPOS;
            double x2 = POINTS[n+1].XPOS;
            double y2 = POINTS[n+1].YPOS;
            double x3 = POINTS[n+2].XPOS;
            double y3 = POINTS[n+2].YPOS;

            // Calculate the area of the three consecutive data points
            double area = Math.abs((x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2))/2);

            if (area > PARAMETERS.AREA1) {
                CMV[3] = true;
                return;
            }
        }
        CMV[3] = false;
    }

    /**
     * Implements LIC 4:
     * There exists at least one set of Q PTS consecutive data points that lie in more than QUADS quadrants.
     * Where there is ambiguity as to which quadrant contains a given point, priority of decision will be
     * by quadrant number, i.e., I, II, III, IV.
     * For example, the data point (0,0) is in quadrant I, the point (-l,0) is in quadrant II,
     * the point (0,-l) is in quadrant III, the point (0,1) is in quadrant I and the point (1,0) is in quadrant I.
     * (2 ≤ Q PTS ≤ NUMPOINTS), (1 ≤ QUADS ≤ 3)
     */
    public void LIC4() {
        // Check input conditions
        if (
                (PARAMETERS.Q_PTS < 2)
                || (PARAMETERS.Q_PTS > NUMPOINTS)
                || (PARAMETERS.QUADS > 3)
                || (PARAMETERS.QUADS < 1)
        ) {
            CMV[4] = false;
            return;
        }

        // start at Q_PTS and look at the sequence of Q_PTS behind iterator i.
        for (int i = PARAMETERS.Q_PTS; i < NUMPOINTS; i++) {

            // used to keep track of which quadrants have been seen in the current sequence.
            int[] quadrants = new int[]{ 0, 0, 0, 0 };

            for (int j = 0; j <= PARAMETERS.Q_PTS; j++) {
                Coordinate point = POINTS[i-j];
                // 1st quadrant
                if (point.XPOS >= 0 && point.YPOS >= 0) {
                    if (quadrants[0] == 0) {
                        quadrants[0] = 1;
                    }
                // 2nd quadrant
                } else if (point.XPOS < 0 && point.YPOS >= 0) {
                    if (quadrants[1] == 0) {
                        quadrants[1] = 1;
                    }
                // 3rd quadrant
                } else if (point.XPOS <= 0 && point.YPOS < 0) {
                    if (quadrants[2] == 0) {
                        quadrants[2] = 1;
                    }
                // 4th quadrant
                } else {
                    if (quadrants[3] == 0) {
                        quadrants[3] = 1;
                    }
                }
            }

            // consecutive data points that lie in _more_ than QUADS quadrants.
            if (Arrays.stream(quadrants).sum() > PARAMETERS.QUADS) {
                CMV[4] = true;
                return;
            }
        }
    }

    public void LIC5() {
        for (int i = 1; i < NUMPOINTS; i++) {
            double diff = POINTS[i].XPOS - POINTS[i-1].XPOS;
            if (diff < 0) {
                CMV[5] = true;
            }
        }
    }
    
    
    /**
     * LIC 7 is:
     * There exists at least one set of two data points separated by exactly K_PTS consecutive
     * intervening points that are a distance greater than the length, LENGTH1, apart.
     * The condition is not met when NUMPOINTS < 3.
     * 1 ≤ K_PTS ≤ (NUMPOINTS − 2)
     */
    public void LIC7() {
        if (
                (NUMPOINTS < 3)
                || (PARAMETERS.K_PTS < 1)
                || (PARAMETERS.K_PTS > NUMPOINTS-2)
        ) {
            CMV[7] = false;
            return;
        }

        for (int n = 0 ; n < NUMPOINTS-1-PARAMETERS.K_PTS ; n++) {
            // Extract the coordinates of the two data points
            double x1 = POINTS[n].XPOS;
            double y1 = POINTS[n].YPOS;
            double x2 = POINTS[n+1+PARAMETERS.K_PTS].XPOS;
            double y2 = POINTS[n+1+PARAMETERS.K_PTS].YPOS;

            // Calculate the distance between the two data points
            double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));

            if (distance > PARAMETERS.LENGTH1) {
                CMV[7] = true;
                return;
            }
        }
        CMV[7] = false;
    }

    /**
     * LIC 9 is:
     * There exists at least one set of three data points separated by exactly C PTS and D PTS consecutive 
     * intervening points, respectively, that form an angle such that: angle < (PI − EPSILON) or angle > (PI + EPSILON)
     * The second point of the set of three points is always the vertex of the angle. If either the first 
     * point or the last point (or both) coincide with the vertex, the angle is undefined and the LIC is 
     * not satisfied by those three points. When NUMPOINTS < 5, the condition is not met.
     */
    public void LIC9(){
        if(NUMPOINTS < 5 || POINTS.length < 5){
            CMV[9] = false;
            return;
        }
        for(int i = 0; i < NUMPOINTS-PARAMETERS.C_PTS-PARAMETERS.D_PTS-2; i++){
            double angle = checkAngle(
                    POINTS[i], 
                    POINTS[i+PARAMETERS.C_PTS+1], 
                    POINTS[i+PARAMETERS.C_PTS+PARAMETERS.D_PTS+2]
            );
            if( angle > Math.PI + PARAMETERS.EPSILON || angle < Math.PI - PARAMETERS.EPSILON){
                CMV[9] = true;
                return;
            }
        }
        CMV[9] = false;
    }

  
    /**
     * LIC 10 is:
     * There exists at least one set of three data points separated by exactly E_PTS and F_PTS consecutive
     * intervening points, respectively, that are the vertices of a triangle with area greater than AREA1.
     * The condition is not met when NUMPOINTS < 5.
     * 1 ≤ E_PTS, 1 ≤ F_PTS, E_PTS + F_PTS ≤ NUMPOINTS−3
     */
    public void LIC10() {
        if (
                (NUMPOINTS < 5)
                || (1 > PARAMETERS.E_PTS)
                || (1 > PARAMETERS.F_PTS)
                || (PARAMETERS.E_PTS + PARAMETERS.E_PTS > NUMPOINTS-3)
        ) {
            CMV[10] = false;
            return;
        }

        for (int n = 0 ; n < NUMPOINTS-2-PARAMETERS.E_PTS-PARAMETERS.F_PTS ; n++) {
            // Extract the coordinates of the three data points
            double x1 = POINTS[n].XPOS;
            double y1 = POINTS[n].YPOS;
            double x2 = POINTS[n+1+PARAMETERS.E_PTS].XPOS;
            double y2 = POINTS[n+1+PARAMETERS.E_PTS].YPOS;
            double x3 = POINTS[n+2+PARAMETERS.E_PTS+PARAMETERS.F_PTS].XPOS;
            double y3 = POINTS[n+2+PARAMETERS.E_PTS+PARAMETERS.F_PTS].YPOS;

            // Calculate the area of the three data points
            double area = Math.abs((x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2))/2);

            if (area > PARAMETERS.AREA1) {
                CMV[10] = true;
                return;
            }
        }
        CMV[10] = false;
    }

    /**
     * LIC 11 is:
     * There exists at least one set of two data points, (X[i],Y[i]) and (X[j],Y[j]),
     * separated by exactly G_PTS consecutive intervening points, such that X[j] - X[i] < 0.
     * (where i < j ) The condition is not met when NUMPOINTS < 3.
     * 1 ≤ G_PTS ≤ NUMPOINTS−2
     */
    public void LIC11() {
        if (
                (NUMPOINTS < 3)
                || (PARAMETERS.G_PTS < 1)
                || (PARAMETERS.G_PTS > NUMPOINTS-2)
        ) {
           CMV[11] = false;
           return;
        }

        // Search after data points separated by G_PTS number of points
        for (int i = 0 ; i < NUMPOINTS-PARAMETERS.G_PTS-1 ; i++) {
            // Extract XPOS for the ith and jth points
            double x_i = POINTS[i].XPOS;
            int j = i + PARAMETERS.G_PTS + 1;
            double x_j = POINTS[j].XPOS;

            // Does X[j] - X[i] < 0 hold
            if (x_j - x_i < 0) {
                CMV[11] = true;
                return;
            }
        }
        CMV[11] = false;
    }
}   
