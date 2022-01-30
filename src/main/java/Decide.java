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


    public void LIC5() {
        for (int i = 1; i < NUMPOINTS; i++) {
            double diff = POINTS[i].XPOS - POINTS[i-1].XPOS;
            if (diff < 0) {
                CMV[5] = true;
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
}   
