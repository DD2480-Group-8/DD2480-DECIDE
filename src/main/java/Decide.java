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
    boolean[][] PUM = new boolean[15][15];
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

    /**
     * Calculates the Preliminary Unlocking Matrix (PUM) using the CMV
     * and LCM.
     */
    public void PUM(){

        //Run all LICs to calculate CMV. Might want to move this later.
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

    public void LIC0() {
        for (int i = 1; i < NUMPOINTS; i++) {
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i - 1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i - 1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }


    /** 
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

    /**
    * Helper function that figures out the angle formed by 3 Coordinates. 
    * The second argument is the vertex of the angle. 
    * @param i First Coordinate
    * @param j Second Coordinate and the vertex of the angle
    * @param k Third Coordinate
    * @return The angle formed by the input Coordinates
    */
    protected double checkAngle(Coordinate i, Coordinate j, Coordinate k){
        i = coordSubtract(i,j);
        k = coordSubtract(k,j);
        if((i.XPOS == 0 && i.YPOS == 0) || (k.XPOS == 0 && k.YPOS == 0)){
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
    protected Coordinate coordSubtract(Coordinate i, Coordinate j){
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
     * LIC 8 is:
     * There exists at least one set of three data points separated by exactly A PTS and B PTS consecutive intervening points, respectively,
     * that cannot be contained within or on a circle of radius RADIUS1. The condition is not met when NUMPOINTS < 5.
     * 1≤A PTS,1≤B PTS
     * A PTS+B PTS ≤ (NUMPOINTS−3)
     */
    public void LIC8() {
        if (NUMPOINTS >= 5) {
            for (int i = 0; i < NUMPOINTS - PARAMETERS.A_PTS - PARAMETERS.B_PTS; i++) {
                // Find the centroid.
                Coordinate centroid = new Coordinate(
                        (POINTS[i].XPOS + POINTS[i+PARAMETERS.A_PTS].XPOS + POINTS[i+PARAMETERS.B_PTS].XPOS) / 3,
                        (POINTS[i].YPOS + POINTS[i+PARAMETERS.A_PTS].YPOS + POINTS[i+PARAMETERS.B_PTS].YPOS) / 3
                );
                // Check if any of the points have a distance to the centroid larger than the radius.
                if (
                        (Math.sqrt(Math.pow(POINTS[i].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                                || (Math.sqrt(Math.pow(POINTS[i+PARAMETERS.A_PTS].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i+PARAMETERS.A_PTS].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                                || (Math.sqrt(Math.pow(POINTS[i+PARAMETERS.B_PTS].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i+PARAMETERS.B_PTS].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                ) {
                    CMV[8] = true;
                    break; // only need one set of points to fulfill this, no need to continue the loop.
                }
            }
        }
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
                || (PARAMETERS.AREA1 < 0)
        ) {
            CMV[10] = false;
            return;
        }

        CMV[10] = threePointsAreaComparison(PARAMETERS.AREA1, true, PARAMETERS.E_PTS, PARAMETERS.F_PTS);
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
     LIC 6 is:
     There exists at least one set of N PTS consecutive data points such that at least one of the
     points lies a distance greater than DIST from the line joining the first and last of these N PTS
     points. If the first and last points of these N PTS are identical, then the calculated distance
     to compare with DIST will be the distance from the coincident point to all other points of
     the N PTS consecutive points. The condition is not met when NUMPOINTS < 3.
     (3 < N PTS < NUMPOINTS), (0 < DIST)
     */
    public void LIC6() {
        if (NUMPOINTS < 3 || PARAMETERS.N_PTS < 3 || PARAMETERS.DIST <= 0) {
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
        if (NUMPOINTS < 5) {
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
    }

}   
