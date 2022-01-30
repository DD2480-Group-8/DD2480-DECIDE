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

    public static void main(String[] args) {
//        System.out.println("Hello World");
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

    public int simpleAddition(int x, int y) {
        return x + y;
    }

    /*
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
        for (int i = 0 ; i < NUMPOINTS - PARAMETERS.N_PTS + 1 ; i++) {
            double[] AB = new double[2];
            same = false;
            //check if [i]th and [i+N_PTS]th coordinate are the same
            if (POINTS[i + PARAMETERS.N_PTS - 1].XPOS == POINTS[i].XPOS
                    && POINTS[i + PARAMETERS.N_PTS - 1].YPOS == POINTS[i].YPOS){
                same = true;
            }
            // form vector AB between the [i]th and [i+N_PTS]th coordinate.
            else{
                AB[0] = POINTS[i + PARAMETERS.N_PTS - 1].XPOS - POINTS[i].XPOS;
                AB[1] = POINTS[i + PARAMETERS.N_PTS - 1].YPOS - POINTS[i].YPOS;
            }
            int j = i+1;
            while (j < i + PARAMETERS.N_PTS - 1)
            {
                // case if points are same
                if (same){
                    double x1 = POINTS[i].XPOS;
                    double y1 = POINTS[i].YPOS;
                    double x2 = POINTS[j].XPOS;
                    double y2 = POINTS[j].YPOS;
                    dis = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
                }
                // vector AP between the [i]th and [j]th coordinate.
                else{
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
                if (dis > PARAMETERS.DIST){
                    CMV[6] = true;
                    return;
                }
                j++;
            }
        }
        CMV[6] = false;
    }
}   
