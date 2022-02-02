import java.util.Arrays;

/**
 * Decide Class
 * Implements all the functionality needed to determine whether the anti-ballistic missile system should launch.
 */
public class Decide {

    /**
     * Operators used in the Logical Connector Matrix.
     */
    enum CONNECTORS {
        /**
         * NOTUSED: CMV[i] and CMV[j] should not prevent any launch, set PUM[i, j] to true.
         */
        NOTUSED,
        /**
         * ORR: Either CMV[i] and CMV[j] have to be true in order for a launch to be approved.
         */
        ORR,
        /**
         * ANDD: Both CMV[i] and CMV[j] have to be true in order for launch to be approved.
         */
        ANDD
    };

    int NUMPOINTS;
    Coordinate[] POINTS;
    CONNECTORS[][] LCM;
    boolean[][] PUM = new boolean[15][15];
    boolean[] CMV = new boolean[15];
    boolean[] PUV;
    boolean[] FUV;
    Parameters PARAMETERS;

    /**
     * Constructor for the Decide Object
     * @param numpts - The number of points/coordinates in the radar data.
     * @param pts - The array of points/coordinates.
     * @param params - The parameters used in the LIC calculations. @see parameters class for details.
     * @param LCM - The Logical Connector Matrix, a matrix of Connector Enum boolean operators.
     * @param PUV - The Preliminary Unlocking Vector, an array of boolean values.
     */
    Decide(int numpts, Coordinate[] pts, Parameters params, CONNECTORS[][] LCM, boolean[] PUV){
        this.NUMPOINTS = numpts;
        this.POINTS = pts;
        this.PARAMETERS = params;
        this.LCM = LCM;
        this.PUV = PUV;

        FUV = new boolean[15];
    }

    /**
     * Main function for the program. Will set up all variables necessary for the program to run.
     * Then call the decide() function that will return a boolean.
     * Based on the return value of decide(), the program will output "yes"/"no",
     * indicating whether the missile should launch.
     *
     */
    public static void main(String[] args) {
        // Set up test variables and create Decide object
        Decide decide = new Decide(2,
                new Coordinate[]{new Coordinate(0, 2), new Coordinate(0, 3)},
                new Parameters(
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1
                ),
                new Decide.CONNECTORS[15][15],
                new boolean[15]);

        // Change variables to make it work

        // LAUNCH Final launch / no launch decision encoded as ”YES”, ”NO” on the standard output.
        boolean launch = decide.decide();
        if (launch) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    /**
     * Launch decision based on the FUV. Condition to launch requires all elements in the FUV to be true from 0 <= i <= 14
     *
     * @return the launch decision
     */
    public boolean launch() {
        for (boolean b : FUV) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the input coordinates is between 2 and 100 (inclusive) points
     * Calls other functions to do necessary calculations and gather information to decide
     * to launch or not.
     * @return true if it is decided to launch
     */
    public boolean decide() {
        // Checks if POINTS is between 2 and 100 (inclusive) planar data points
        if ((POINTS.length >= 2) && (POINTS.length <= 100)) {
            return false;
        }

        runLIC();

        // Calculates the PUM
        PUM();
        // Calculates the FUV
        calculateFUV();

        return launch();
    }

    public void runLIC() {
        //Run all LICs to calculate CMV
        LIC0();
        LIC1();
        LIC2();
        LIC3();
        LIC4();
        LIC5();
        LIC6();
        LIC7();
        LIC8();
        LIC9();
        LIC10();
        LIC11();
        LIC12();
        LIC13();
        LIC14();
    }

    /**
     * Function that calculates and sets all values of the FUV
     */
    public void calculateFUV() {
        // Loops through the FUV
        for (int i = 0 ; i < FUV.length ; i++) {
            // If PUV[i] is false, then FUV[i] should be set to true
            if (!PUV[i]) {
                FUV[i] = true;
            } else {
                boolean FUVTempValue = true;
                // Checks that the whole row PUM[i] is all true
                for (int j = 0 ; j < PUM[i].length ; j++) {
                    // If one element is false, FUV[i] should be set to false
                    if (!PUM[i][j]) {
                        FUVTempValue = false;
                        break;
                    }
                }
                FUV[i] = FUVTempValue;
            }
        }
    }

    /**
     * Calculates the Preliminary Unlocking Matrix (PUM) using the CMV
     * and LCM.
     */
    public void PUM(){
        
        //Calculate PUM values for each LIC pair using LCM matrix. 
        for(int i = 0; i < PUM.length; i++){
            for(int j = 0; j < PUM[0].length; j++){
                switch(LCM[i][j]){
                    case NOTUSED:
                        PUM[i][j] = true;
                        break;
                    case ANDD:
                        PUM[i][j] = (CMV[i] && CMV[j]);
                        break;
                    case ORR:
                        PUM[i][j] = CMV[i] || CMV[j];
                        break;
                }
            }
        }
    }

    /**
     * LIC0 is:
     * There exists at least one set of two consecutive data points that are a distance greater than the length, LENGTH1, apart.
     * (0 ≤ LENGTH1)
     */
    public void LIC0() {
        if(PARAMETERS.LENGTH1 < 0){
            CMV[0] = false;
            return;
        }
        for (int i = 1; i < NUMPOINTS; i++) {
            if (calculateDistance(POINTS[i], POINTS[i-1]) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
                return;
            }
        }
        CMV[0] = false;
    }

    /**
     * Implementation of LIC1: There exists at least one set of three consecutive data points that cannot all be contained
     * within or on a circle of radius RADIUS1.
     * Finds the center between three points, and checks if there is any point that is a distance greater than RADIUS1
     * from the center.
     */
    public void LIC1() {
        if(PARAMETERS.RADIUS1 < 0){
            CMV[1] = false;
            return; 
        }
        for (int i = 2; i < NUMPOINTS; i++) {
            
            // Check if these points don't fit in a circle of the radius 
            if (checkCircle(POINTS[i], POINTS[i-1], POINTS[i-2])) {
                CMV[1] = true;
                return; // only need one set of points to fulfill this, no need to continue the loop.
            }
        }
        CMV[1] = false;
    }

    /** 
    * Implementation of LIC2: There exists at least one set of three consecutive data points which form an angle such that: angle < (PI − EPSILON)
    * or angle > (PI + EPSILON). The second of the three consecutive points is always the vertex of the angle. If either the first point or the 
    * last point (or both) coincides with the vertex, the angle is undefined and the LIC is not satisfied by those three points.
    */
    public void LIC2(){
        if(     
                PARAMETERS.EPSILON < 0 
                || PARAMETERS.EPSILON >= Math.PI 
                || NUMPOINTS < 3
        ) {
            CMV[1] = false;
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

    /**
    *   LIC 3 is:
    *   There exists at least one set of three consecutive data points that are the vertices of a triangle
    *   with area greater than AREA1. (0 ≤ AREA1)
    *
    */
    public void LIC3(){
        if (
                (NUMPOINTS < 3)
                || (PARAMETERS.AREA1 < 0)
        ) {
            CMV[3] = false;
            return;
        }
        // offset and offset2 set to 0 since there are no intervening data points
        CMV[3] = threePointsAreaComparison(PARAMETERS.AREA1, true, 0,0);
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
                    quadrants[0] = 1;
                // 2nd quadrant
                } else if (point.XPOS < 0 && point.YPOS >= 0) {
                    quadrants[1] = 1;
                // 3rd quadrant
                } else if (point.XPOS <= 0 && point.YPOS < 0) {
                    quadrants[2] = 1;
                // 4th quadrant
                } else {
                    quadrants[3] = 1;
                }
            }

            // consecutive data points that lie in _more_ than QUADS quadrants.
            if (Arrays.stream(quadrants).sum() > PARAMETERS.QUADS) {
                CMV[4] = true;
                return;
            }
        }
        CMV[4] = false;
    }

    /**
     * LIC 5 is:
     * There exists at least one set of two consecutive data points, (X[i],Y[i]) and (X[j],Y[j]), such
     * that X[j] - X[i] < 0. (where i = j-1)
     */
    public void LIC5() {
        for (int i = 1; i < NUMPOINTS; i++) {
            double diff = POINTS[i].XPOS - POINTS[i-1].XPOS;
            if (diff < 0) {
                CMV[5] = true;
                return;
            }
        }
        CMV[5] = false;
    }

    /**
     LIC 6 is:
     There exists at least one set of N PTS consecutive data points such that at least one of the
     points lies a distance greater than DIST from the line joining the first and last of these N PTS
     points. If the first and last points of these N PTS are identical, then the calculated distance
     to compare with DIST will be the distance from the coincident point to all other points of
     the N PTS consecutive points. The condition is not met when NUMPOINTS < 3.
     (3 < N PTS < NUMPOINTS), (0 < DIST)
     */
    public void LIC6() {
        if (
                NUMPOINTS < 3 
                || PARAMETERS.N_PTS < 3 
                || PARAMETERS.DIST <= 0
        ) {
            CMV[6] = false;
            return;
        }
        double dis;
        boolean same;
        for (int i = 0; i < NUMPOINTS - PARAMETERS.N_PTS + 1; i++) {
            double[] AB = new double[2];
            same = false;
            //check if [i]th and [i+N_PTS]th coordinate are the same
            if (POINTS[i + PARAMETERS.N_PTS - 1].XPOS == POINTS[i].XPOS
                    && POINTS[i + PARAMETERS.N_PTS - 1].YPOS == POINTS[i].YPOS) {
                same = true;
            }
            // form vector AB between the [i]th and [i+N_PTS]th coordinate.
            else {
                AB[0] = POINTS[i + PARAMETERS.N_PTS - 1].XPOS - POINTS[i].XPOS;
                AB[1] = POINTS[i + PARAMETERS.N_PTS - 1].YPOS - POINTS[i].YPOS;
            }
            int j = i + 1;
            while (j < i + PARAMETERS.N_PTS - 1) {
                // case if points are same
                if (same) {
                    double x1 = POINTS[i].XPOS;
                    double y1 = POINTS[i].YPOS;
                    double x2 = POINTS[j].XPOS;
                    double y2 = POINTS[j].YPOS;
                    dis = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
                }
                // vector AP between the [i]th and [j]th coordinate.
                else {
                    double[] AP = new double[2];
                    AP[0] = POINTS[j].XPOS - POINTS[i].XPOS;
                    AP[1] = POINTS[j].YPOS - POINTS[i].YPOS;

                    // Finding the perpendicular distance using |(AB X AP)/|AB||
                    double x1 = AB[0];
                    double y1 = AB[1];
                    double x2 = AP[0];
                    double y2 = AP[1];
                    double mod = Math.sqrt(x1 * x1 + y1 * y1);
                    dis = Math.abs(x1 * y2 - y1 * x2) / mod;
                }
                //check if within DIST
                if (dis > PARAMETERS.DIST) {
                    CMV[6] = true;
                    return;
                }
                j++;
            }
        }
        CMV[6] = false;
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
            // Calculate the distance between the two data points
            double distance = calculateDistance(POINTS[n], POINTS[n+1+PARAMETERS.K_PTS]);

            if (distance > PARAMETERS.LENGTH1) {
                CMV[7] = true;
                return;
            }
        }
        CMV[7] = false;
    }

    /**
     * LIC 8 is:
     * There exists at least one set of three data points separated by exactly A PTS and B PTS consecutive intervening points, respectively,
     * that cannot be contained within or on a circle of radius RADIUS1. The condition is not met when NUMPOINTS < 5.
     * 1≤A PTS,1≤B PTS
     * A PTS+B PTS ≤ (NUMPOINTS−3)
     */
    public void LIC8() {
        if (
                NUMPOINTS < 5
                || PARAMETERS.A_PTS < 1
                || PARAMETERS.B_PTS < 1
                || PARAMETERS.A_PTS + PARAMETERS.B_PTS > NUMPOINTS - 3
        ) {
            CMV[8] = false;
            return;
        }
        for (int i = 0; i < NUMPOINTS - PARAMETERS.A_PTS - PARAMETERS.B_PTS - 2; i++) {
             
            // Check if these points don't fit in a circle of the radius
            if (checkCircle(POINTS[i], POINTS[i+1], POINTS[i+2])) {
                CMV[8] = true;
                return; // only need one set of points to fulfill this, no need to continue the loop.
            }
            
        }
        CMV[8] = false;
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
        if(
                NUMPOINTS < 5 
                || POINTS.length < 5
                || PARAMETERS.EPSILON < 0 
                || PARAMETERS.EPSILON >= Math.PI 
                || PARAMETERS.C_PTS < 1
                || PARAMETERS.D_PTS < 1
                ||  PARAMETERS.C_PTS + PARAMETERS.D_PTS > NUMPOINTS - 3
        ){
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
                || (PARAMETERS.AREA1 < 0)
        ) {
            CMV[10] = false;
            return;
        }

        CMV[10] = threePointsAreaComparison(PARAMETERS.AREA1, true, PARAMETERS.E_PTS, PARAMETERS.F_PTS);
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

    /**
     * LIC12 implements:
     * There exists at least one set of two data points, separated by exactly K PTS consecutive intervening points,
     * which are a distance greater than the length, LENGTH1, apart. In addition,
     * there exists at least one set of two data points (which can be the same or different from the two data points just mentioned),
     * separated by exactly K PTS consecutive intervening points, that are a distance less than the length, LENGTH2, apart.
     * Both parts must be true for the LIC to be true.
     * The condition is not met when NUMPOINTS < 3.
     * 0 ≤ LENGTH2
     */
    public void LIC12() {
        // Check input conditions
        if (
                (NUMPOINTS < 3)
                || (PARAMETERS.LENGTH1 < 0) // I'm assuming a length cannot be negative.
                || (PARAMETERS.LENGTH2 < 0)
                || (PARAMETERS.K_PTS < 0) // we have to have 2 endpoints and at least k points in between.
        ) {
            CMV[12] = false;
            return;
        }

        // We have two conditions that have to be true for the LIC, flip their corresponding bit once they are fulfilled.
        int[] conditions = new int[]{ 0, 0 };
        // + 1 and -1 for the start-and endpoint since we have to have K_PTS _between_ them.
        for (int i = PARAMETERS.K_PTS + 1; i < NUMPOINTS; i++) {
            Coordinate endpoint = POINTS[i];
            Coordinate startpoint = POINTS[i - PARAMETERS.K_PTS - 1];

            // Get the distance between the two coordinates
            double distance = Math.sqrt(Math.pow(endpoint.XPOS - startpoint.XPOS, 2) + Math.pow(endpoint.YPOS - startpoint.YPOS, 2));

            // Condition 1: which are a distance greater than the length, LENGTH1, apart.
            if (distance > PARAMETERS.LENGTH1) {
                conditions[0] = 1;
            }

            // Condition 2: that are a distance less than the length, LENGTH2, apart. Can be the same points that fulfilled condition 1.
            if (distance < PARAMETERS.LENGTH2) {
                conditions[1] = 1;
            }

            // If both conditions are fulfilled, set CMV[12] to true.
            if (Arrays.stream(conditions).sum() == 2) {
                CMV[12] = true;
                return;
            }
        }
        CMV[12] = false;
    }

    /**
     LIC 13 is:
     There exists at least one set of three data points, separated by exactly A PTS and B PTS
     consecutive intervening points, respectively, that cannot be contained within or on a circle of
     radius RADIUS1. In addition, there exists at least one set of three data points (which can be
     the same or different from the three data points just mentioned) separated by exactly A PTS
     and B PTS consecutive intervening points, respectively, that can be contained in or on a
     circle of radius RADIUS2. Both parts must be true for the LIC to be true. The condition is
     not met when NUMPOINTS < 5.
     0 < RADIUS2
     */
    public void LIC13() {
        if (NUMPOINTS < 5 || PARAMETERS.RADIUS2 < 0) {
            CMV[13] = false;
            return;
        }
        // Initialise booleans for both conditions as false once
        boolean con1 = false;
        boolean con2 = false;
        for (int i = 0 ; i < NUMPOINTS - PARAMETERS.A_PTS - PARAMETERS.B_PTS - 2 ; i++) {
            // Extract the coordinates of the three data points
            double x1 = POINTS[i].XPOS;
            double y1 = POINTS[i].YPOS;
            double x2 = POINTS[i + 1 + PARAMETERS.E_PTS].XPOS;
            double y2 = POINTS[i + 1 + PARAMETERS.E_PTS].YPOS;
            double x3 = POINTS[i + 2 + PARAMETERS.E_PTS + PARAMETERS.F_PTS].XPOS;
            double y3 = POINTS[i + 2 + PARAMETERS.E_PTS + PARAMETERS.F_PTS].YPOS;

            // Find the centroid.
            Coordinate centroid = new Coordinate(
                    (x1 + x2 + x3) / 3,
                    (y1 + y2 + y3) / 3
            );
            // Check if any of the points have a distance to the centroid larger than RADIUS1.
            if (
                    (Math.sqrt(Math.pow(x1 - centroid.XPOS, 2) + Math.pow(y1 - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                            || (Math.sqrt(Math.pow(x2 - centroid.XPOS, 2) + Math.pow(y2 - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                            || (Math.sqrt(Math.pow(x3 - centroid.XPOS, 2) + Math.pow(y3 - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
            ){
                con1 = true;
            }
            // Check if all the points have a distance to the centroid smaller than RADIUS2.
            if (
                    (Math.sqrt(Math.pow(x1 - centroid.XPOS, 2) + Math.pow(y1 - centroid.YPOS, 2)) < PARAMETERS.RADIUS2)
                            && (Math.sqrt(Math.pow(x2 - centroid.XPOS, 2) + Math.pow(y2 - centroid.YPOS, 2)) < PARAMETERS.RADIUS2)
                            && (Math.sqrt(Math.pow(x3 - centroid.XPOS, 2) + Math.pow(y3 - centroid.YPOS, 2)) < PARAMETERS.RADIUS2)
            ){
                con2 = true;
            }
            if (con1 && con2){
                CMV[13] = true;
                return;
            }
        }
        CMV[13] = false;
    }

    /**
     * LIC 14 is:
     * There exists at least one set of three data points, separated by exactly E_PTS and F_PTS
     * consecutive intervening points, respectively, that are the vertices of a triangle with area
     * greater than AREA1. In addition, there exist three data points (which can be the same or
     * different from the three data points just mentioned) separated by exactly E_PTS and F_PTS
     * consecutive intervening points, respectively, that are the vertices of a triangle with area
     * less than AREA2. Both parts must be true for the LIC to be true.
     * The condition is not met when NUMPOINTS < 5.
     * 0 ≤ AREA2
     */
    public void LIC14() {
        if (
                (NUMPOINTS < 5)
                || (1 > PARAMETERS.E_PTS)
                || (1 > PARAMETERS.F_PTS)
                || (PARAMETERS.E_PTS + PARAMETERS.E_PTS > NUMPOINTS-3)
                || (PARAMETERS.AREA1 < 0)
                || (PARAMETERS.AREA2 < 0)
        ) {
            CMV[14] = false;
            return;
        }

        boolean area2Greater = false;

        boolean area1Greater = threePointsAreaComparison(PARAMETERS.AREA1, true, PARAMETERS.E_PTS, PARAMETERS.F_PTS);

        if (area1Greater) {
            area2Greater = threePointsAreaComparison(PARAMETERS.AREA2, false, PARAMETERS.E_PTS, PARAMETERS.F_PTS);
        }
        // Set to true only if both conditions are true
        CMV[14] = area1Greater && area2Greater;
    }

    // HELPER FUNCTIONS

    /**
     * Calculates the euclidian distance between two Coordinates.
     * @param i Coordinate 1
     * @param j Coordinate 2
     * @return Euclidian distance between Coordinate 1 & 2
     */
    protected double calculateDistance(Coordinate i, Coordinate j) {
         // Extract the coordinates of the two data points
        double x1 = i.XPOS;
        double y1 = i.YPOS;
        double x2 = j.XPOS;
        double y2 = j.YPOS;

        // Calculate and return the distance between the two data points
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }
    
    /**
     * Checks if three Coordinates fit do not fit inside or on a circle of radius PARAMETERS.RADIUS1.
     * @param i first Coordinate
     * @param j second Coordinate
     * @param k thirs Coordinate
     * @return True if the points do not fit inside or on a circle of area PARAMETERS.RADIUS1, false otherwise
     */
    protected boolean checkCircle(Coordinate i, Coordinate j, Coordinate k) {
        Coordinate centroid = new Coordinate(
                        (i.XPOS + j.XPOS + k.XPOS) / 3,
                        (i.YPOS + j.YPOS + k.YPOS) / 3
                );
        
            if (
                (Math.sqrt(Math.pow(i.XPOS - centroid.XPOS, 2) + Math.pow(i.YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                        || (Math.sqrt(Math.pow(j.XPOS - centroid.XPOS, 2) + Math.pow(j.YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                        || (Math.sqrt(Math.pow(k.XPOS - centroid.XPOS, 2) + Math.pow(k.YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
            ){
                return true;
            }else {
                return false;
            }
    }

    /**
     * Help function that finds three consecutive points and tests if their area is greater than the input area
     *
     * @param area Area to be compared against
     * @param shouldBeGreater A boolean to know if the thisArea should be greater or less than input area
     * @param offset Number of points that should be intervening the first and second point in the triangle
     * @param offset2 Number of points that should be intervening the second and third point in the triangle
     * @return True if an area is found that is greater than "area"
     */
    protected boolean threePointsAreaComparison(double area, boolean shouldBeGreater, int offset, int offset2) {
        for (int n = 0 ; n < NUMPOINTS-2-offset-offset2 ; n++) {
            double thisArea = calculateArea(
                    POINTS[n],
                    POINTS[n+1+offset],
                    POINTS[n+2+offset+offset2]
            );

            // If-condition to know if thisArea should be greater or less than area
            if (shouldBeGreater) {
                if (thisArea >= area) {
                    return true;
                }
            } else {
                if (thisArea <= area) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Takes in three coordinates and calculates the area between them
     *
     * @param i The first Coordinate
     * @param j The second Coordinate
     * @param k The third Coordinate
     * @return The area of the three Coordinates
     */
    protected double calculateArea(Coordinate i, Coordinate j, Coordinate k) {
        // Extract the coordinates of the three data points
        double x1 = i.XPOS;
        double y1 = i.YPOS;
        double x2 = j.XPOS;
        double y2 = j.YPOS;
        double x3 = k.XPOS;
        double y3 = k.YPOS;

        // Calculate the area of the three data points and returns it
        return Math.abs((x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2))/2);
    }

    /**
     * Helper function that figures out the angle formed by 3 Coordinates.
     * The second argument is the vertex of the angle.
     * @param i First Coordinate
     * @param j Second Coordinate and the vertex of the angle
     * @param k Third Coordinate
     * @return The angle formed by the input Coordinates
     */
    protected double checkAngle(Coordinate i, Coordinate j, Coordinate k) {
        i = coordSubtract(i,j);
        k = coordSubtract(k,j);
        if((i.XPOS == 0 && i.YPOS == 0) || (k.XPOS == 0 && k.YPOS == 0)) {
            return Math.PI;
        }
        double dotProduct = i.XPOS * k.XPOS + i.YPOS * k.YPOS;
        double lenProduct = Math.sqrt(Math.pow(i.XPOS,2) + Math.pow(i.YPOS,2)) * Math.sqrt(Math.pow(k.XPOS,2) + Math.pow(k.YPOS,2));
        return Math.acos(dotProduct/lenProduct);
    }

    /**
     * Helper function that subtracts one Coordinate from another.
     * @param i Coordinate to be subtracted from
     * @param j Coordinate to subtract
     * @return The resulting Coordinate after the subtraction.
     */
    protected Coordinate coordSubtract(Coordinate i, Coordinate j) {
        Coordinate res = new Coordinate(0,0);
        res.XPOS = i.XPOS - j.XPOS;
        res.YPOS = i.YPOS - j.YPOS;
        return(res);
    }
}   
